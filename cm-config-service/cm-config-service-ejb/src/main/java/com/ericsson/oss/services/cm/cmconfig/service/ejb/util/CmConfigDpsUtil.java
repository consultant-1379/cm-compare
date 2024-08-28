/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2012
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.services.cm.cmconfig.service.ejb.util;

import static com.ericsson.oss.services.cm.cmconfig.service.api.CmConfigConstants.DPS_LOCAL_JNDI;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;

import com.ericsson.oss.itpf.datalayer.dps.DataBucket;
import com.ericsson.oss.itpf.datalayer.dps.DataPersistenceService;
import com.ericsson.oss.itpf.datalayer.dps.persistence.ManagedObject;
import com.ericsson.oss.itpf.datalayer.dps.persistence.PersistenceObject;
import com.ericsson.oss.itpf.datalayer.dps.query.ObjectField;
import com.ericsson.oss.itpf.datalayer.dps.query.Query;
import com.ericsson.oss.itpf.datalayer.dps.query.QueryBuilder;
import com.ericsson.oss.itpf.datalayer.dps.query.QueryExecutor;
import com.ericsson.oss.itpf.datalayer.dps.query.Restriction;
import com.ericsson.oss.itpf.datalayer.dps.query.RestrictionBuilder;
import com.ericsson.oss.itpf.datalayer.dps.query.StringMatchCondition;
import com.ericsson.oss.itpf.datalayer.dps.query.TypeRestrictionBuilder;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmMatchCondition;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchCriteria;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchScope;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchScope.ScopeType;

/**
 * A set of utility methods for interacting with the DPS.
 */
public class CmConfigDpsUtil {

    /**
     * OSS model namespace.
     */
    public static final String OSS_TOP_NAMESPACE = "OSS_TOP";

    /**
     * MeContext name.
     */
    public static final String ME_CONTEXT_MO_NAME = "MeContext";

    @EJB(lookup = DPS_LOCAL_JNDI)
    private DataPersistenceService dataPersistenceService;

    /**
     * Gets the FDN.
     *
     * @param cmSearchScope
     *            search scope
     * @return FDN
     */
    public String getFDN(final CmSearchScope cmSearchScope) {
        return cmSearchScope.getName() + "=" + cmSearchScope.getValue();
    }

    /**
     * Gets MO based on FDN.
     *
     * @param fdn
     *            FDN
     * @param config
     *            DPS Bucket
     * @return MO
     */
    public ManagedObject getManagedElementByFdn(final String fdn, final DataBucket config) {
        return config.findMoByFdn(fdn);
    }

    /**
     * Deletes MO.
     *
     * @param managedObject
     *            DPS MO
     * @param bucket
     *            DPS Bucket
     * @return delete status
     */
    public int deleteManagedElementByMoReference(final ManagedObject managedObject, final DataBucket bucket) {
        return bucket.deletePo(managedObject);
    }

    /**
     * Gets the MOs based on the search scope.
     *
     * @param config
     *            DPS Bucket
     * @param cmSearchScope
     *            CM Search scope
     * @return MOs
     */
    public List<ManagedObject> findNesFromSearchScope(final DataBucket config, final CmSearchScope cmSearchScope) {
        final List<ManagedObject> managedObjects = new ArrayList<>();
        if (cmSearchScope.getScopeType() == ScopeType.NODE_NAME) {
            // Check if the searchScope is single node
            if (cmSearchScope.getCmMatchCondition() == CmMatchCondition.EQUALS) {
                final ManagedObject managedObject = getManagedElementByFdn(getFDN(cmSearchScope), config);
                if (managedObject != null) {
                    managedObjects.add(managedObject);
                }
            } else {
                managedObjects.addAll(findManagedObjectsByQuery(config, cmSearchScope));
            }
        } else if (cmSearchScope.getScopeType() == ScopeType.UNSPECIFIED) {
            managedObjects.addAll(findManagedObjectsByQuery(config, cmSearchScope));
        }
        return managedObjects;
    }

    /**
     * Gets MOs by query.
     *
     * @param config
     *            DPS bucket
     * @param cmSearchScope
     *            CM search scope
     * @return MOs.
     */
    private List<ManagedObject> findManagedObjectsByQuery(final DataBucket config, final CmSearchScope cmSearchScope) {
        final Query<? extends RestrictionBuilder> query = findTypeQueryFromSearchScope(cmSearchScope);
        final List<ManagedObject> managedObjects = executeQuery(config, query);
        return managedObjects;
    }

    /**
     * Gets type query from search scope.
     *
     * @param cmSearchScope
     *            CM search scope
     * @return query
     */
    private Query<? extends RestrictionBuilder> findTypeQueryFromSearchScope(final CmSearchScope cmSearchScope) {
        // retrieves the QueryBuilder which provides builder to create queries in DPS
        final QueryBuilder queryBuilder = this.dataPersistenceService.getQueryBuilder();

        // create the desired TypeQuery that will fetch all objects from the database with namespace and type specified
        String typeName = cmSearchScope.getName();
        if (typeName == null) {
            typeName = ME_CONTEXT_MO_NAME;
        }

        final Query<TypeRestrictionBuilder> typeQuery = queryBuilder.createTypeQuery(OSS_TOP_NAMESPACE, typeName);

        // apply restriction to scope type NODE_NAME (UNSPECIFIED DOES NOT NEED ANY RESTRICTION)
        if (cmSearchScope.getScopeType() == ScopeType.NODE_NAME) {
            // creates restriction that filters the above query by fetching only the objects with math the value.
            final Restriction restriction = typeQuery.getRestrictionBuilder().matchesString(ObjectField.MO_NAME, cmSearchScope.getValue(),
                    getStringMatchConditionFromCmMathCondition(cmSearchScope.getCmMatchCondition()));
            typeQuery.setRestriction(restriction);
        }

        return typeQuery;
    }

    /**
     * Gets a @{code StringMatchCondition} from a {@code CmMatchCondition}.
     *
     * @param cmMatchCondition
     *            CM Match condition
     * @return the match condition
     */
    private StringMatchCondition getStringMatchConditionFromCmMathCondition(final CmMatchCondition cmMatchCondition) {
        StringMatchCondition stringMatchCondition = null;
        switch (cmMatchCondition) {
            case STARTS_WITH:
                stringMatchCondition = StringMatchCondition.STARTS_WITH;
                break;
            case ENDS_WITH:
                stringMatchCondition = StringMatchCondition.ENDS_WITH;
                break;
            case CONTAINS:
                stringMatchCondition = StringMatchCondition.CONTAINS;
                break;
        }
        return stringMatchCondition;
    }

    /**
     * Executes a DPS Query.
     *
     * @param config
     *            DPS bucket
     * @param query
     *            DPS query
     * @return MOs
     */
    private List<ManagedObject> executeQuery(final DataBucket config, final Query<? extends RestrictionBuilder> query) {
        // retrieve the query executor
        final QueryExecutor queryExecutor = config.getQueryExecutor();
        final Iterator<PersistenceObject> poIterator = queryExecutor.execute(query);
        final List<ManagedObject> managedObjects = new ArrayList<>();
        while (poIterator.hasNext()) {
            final PersistenceObject persistenceObject = poIterator.next();
            managedObjects.add((ManagedObject) persistenceObject);
        }
        return managedObjects;
    }

    /**
     * Gets the common NEs across buckets.
     *
     * @param targetBucket
     *            DPS bucket
     * @param referenceBucket
     *            DPS bucket
     * @param scope
     *            CM search scope
     * @return common MOs
     */
    public Collection<ManagedObject> findNEsCommonAcrossBuckets(final DataBucket targetBucket, final DataBucket referenceBucket,
            final CmSearchCriteria scope) {

        final Collection<ManagedObject> nesInTarget = findNesFromSearchCriteria(targetBucket, scope);

        final Collection<ManagedObject> commonMOs = new HashSet<>();
        for (final ManagedObject targetNe : nesInTarget) {
            final ManagedObject referenceBucketMo = referenceBucket.findMoByFdn(targetNe.getFdn());
            if (referenceBucketMo != null) {
                commonMOs.add(referenceBucketMo);
            }
        }
        return commonMOs;
    }

    private Collection<ManagedObject> findNesFromSearchCriteria(final DataBucket targetBucket, final CmSearchCriteria scope) {
        final List<CmSearchScope> cmSearchScopes = new ArrayList<CmSearchScope>();
        if (scope.getCmSearchScopes().isEmpty()) {
            final CmSearchScope allNodesScope = new CmSearchScope();
            allNodesScope.setScopeType(ScopeType.UNSPECIFIED);
            cmSearchScopes.add(allNodesScope);
        } else {
            cmSearchScopes.addAll(scope.getCmSearchScopes());
        }
        final Set<ManagedObject> nesInTarget = new HashSet<>();
        for (final CmSearchScope cmSearchScope : cmSearchScopes) {
            final List<ManagedObject> nodes = findNesFromSearchScope(targetBucket, cmSearchScope);
            nesInTarget.addAll(nodes);
        }
        return nesInTarget;
    }

    /**
     * Get NEs which exist only in this bucket.
     * (All NEs in this bucket - Common NEs)
     *
     * @param bucket
     *            bucket
     * @param commonMos
     *            common Mos
     * @return
     *         mos of NEs which exist only in this bucket
     */
    public Collection<ManagedObject> findNEsOnlyInThisBucket(final DataBucket bucket, final Collection<ManagedObject> commonMos) {
        final CmSearchScope cmSearchScope = new CmSearchScope();
        final List<ManagedObject> allNesInThisBucket = findNesFromSearchScope(bucket, cmSearchScope);
        final Collection<ManagedObject> mosOnlyInthisBucket = new HashSet<>();
        for (final ManagedObject ne : allNesInThisBucket) {
            boolean matchFound = false;
            for (final ManagedObject commonMo : commonMos) {
                if (commonMo.getFdn().equals(ne.getFdn())) {
                    matchFound = true;
                    break;
                }
            }
            if (!matchFound) {
                mosOnlyInthisBucket.add(ne);
            }
        }
        return mosOnlyInthisBucket;
    }

    /**
     * Returns all NEs in two buckets.
     *
     * @param targetBucket
     *            DPS bucket
     * @param referenceBucket
     *            DPS bucket
     * @param searchCriteria
     *            CM search scope
     * @return all MOs
     */
    public Collection<ManagedObject> findAllNEsFromBuckets(final DataBucket targetBucket, final DataBucket referenceBucket,
            final CmSearchCriteria searchCriteria) {
        final Collection<ManagedObject> unionMos = new HashSet<>();

        CmSearchScope cmSearchScope;
        if (!searchCriteria.getCmSearchScopes().isEmpty()) {
            cmSearchScope = searchCriteria.getCmSearchScopes().get(0);
        } else {
            cmSearchScope = new CmSearchScope();
        }
        final List<ManagedObject> nesInTarget = findNesFromSearchScope(targetBucket, cmSearchScope);
        final List<ManagedObject> nesInReference = findNesFromSearchScope(referenceBucket, cmSearchScope);

        final Iterator<ManagedObject> targetMoIterator = nesInTarget.iterator();
        unionMos.addAll(findMoCollection(targetMoIterator));

        final Iterator<ManagedObject> referenceMoIterator = nesInReference.iterator();
        unionMos.addAll(findMoCollection(referenceMoIterator));

        return unionMos;
    }

    private Collection<ManagedObject> findMoCollection(final Iterator<ManagedObject> thisMoIterator) {
        final Collection<ManagedObject> mos = new ArrayList<>();
        while (thisMoIterator.hasNext()) {
            mos.add(thisMoIterator.next());
        }
        return mos;
    }
}
