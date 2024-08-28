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
package com.ericsson.oss.services.cm.writer.test;

import java.util.Iterator;

import javax.inject.Inject;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;

import com.ericsson.oss.itpf.datalayer.dps.DataBucket;
import com.ericsson.oss.itpf.datalayer.dps.persistence.ManagedObject;
import com.ericsson.oss.itpf.datalayer.dps.persistence.PersistenceObject;
import com.ericsson.oss.itpf.datalayer.dps.query.Query;
import com.ericsson.oss.itpf.datalayer.dps.query.QueryBuilder;
import com.ericsson.oss.services.cm.cmshared.dto.CmObject;
import com.ericsson.oss.services.cm.cmtools.dto.mapping.DpsObjectMapper;
import com.ericsson.oss.services.cm.cmtools.dto.mapping.DpsObjectMapperImpl;

public class DpsTestBase {
    protected static final String NODE_NAME1 = "ERBS1";
    protected static final String NODE_NAME2 = "ERBS_NODE_2"; // Deliberate different naming pattern for search use cases
    protected static final String DEFAULT_MO_ID = "1";
    protected static final String EUCFDD1_ID = "EUC001";

    protected CmObject meContextAsCmObject;
    protected CmObject managedElementAsCmObject;
    protected CmObject eNodeBFunctionAsCmObject;
    protected CmObject sectorEquipmentFunctionAsCmObject;
    protected CmObject eUtranCellFDDAsCmObject;

    @Inject
    protected Logger logger;

    @Inject
    protected DpsFacade dpsFacade;

    @Inject
    protected UserTransaction userTransaction;

    DpsObjectMapper dpsManagedObjectMapper = new DpsObjectMapperImpl();

    /**
     * T R A N S A C T I O N H A N D L I N G
     */
    protected void rollbackTransactionIfActive() throws Exception {
        if (isActive(userTransaction) || isMarkedForRollback(userTransaction)) {
            userTransaction.rollback();
        }
    }

    private boolean isMarkedForRollback(final UserTransaction utx) {
        try {
            final int status = utx.getStatus();
            return status == Status.STATUS_MARKED_ROLLBACK;
        } catch (final SystemException e) {
            return false;
        }
    }

    private boolean isActive(final UserTransaction utx) {
        try {
            final int status = utx.getStatus();
            return status == Status.STATUS_ACTIVE;
        } catch (final SystemException e) {
            return false;
        }
    }

    /**
     * T O P O L O G Y H A N D L I N G
     */
    @Before
    public void insertData() throws Exception {
        deleteAllNodes();
        // TODO EEITSIK probably need arrays to store all global node-information for tests..
        // For now ensure ERBS1 gets create LAST so that those variables will point to that node
        createNode(NODE_NAME2);
        createNode(NODE_NAME1);
    }

    @After
    public void cleanUp() throws Exception {
        try {
            deleteAllNodes();
        } finally {
            rollbackTransactionIfActive();
        }
    }

    protected void createNode(final String nodeName) throws Exception {
        userTransaction.begin();
        final DataBucket liveBucket = dpsFacade.getLiveBucket();

        final ManagedObject createdMeContextMO = createRootTestMOInDPS(liveBucket, DpsTestMoType.MECONTEXT, nodeName);
        meContextAsCmObject = dpsManagedObjectMapper.mapToCmObject(createdMeContextMO);

        final ManagedObject createdManagedElementMO = createTestMOInDPS(liveBucket, DpsTestMoType.MANAGED_ELEMENT, createdMeContextMO, DEFAULT_MO_ID,
                nodeName);
        managedElementAsCmObject = dpsManagedObjectMapper.mapToCmObject(createdManagedElementMO);

        final ManagedObject createdENodeBFunctionMO = createTestMOInDPS(liveBucket, DpsTestMoType.ENODEB_FUNCTION, createdManagedElementMO,
                DEFAULT_MO_ID, nodeName);
        eNodeBFunctionAsCmObject = dpsManagedObjectMapper.mapToCmObject(createdENodeBFunctionMO);

        final ManagedObject createdSectorEquipmentFunctionMO = createTestMOInDPS(liveBucket, DpsTestMoType.SECTOR_EQUIPMENT_FUNCTION,
                createdManagedElementMO, DEFAULT_MO_ID, nodeName);
        sectorEquipmentFunctionAsCmObject = dpsManagedObjectMapper.mapToCmObject(createdSectorEquipmentFunctionMO);

        final ManagedObject createdEUtranCellFDDMO = createTestMOInDPS(liveBucket, DpsTestMoType.EUTRAN_CELL_FDD, createdENodeBFunctionMO,
                EUCFDD1_ID, nodeName);
        eUtranCellFDDAsCmObject = dpsManagedObjectMapper.mapToCmObject(createdEUtranCellFDDMO);

        userTransaction.commit();
    }

    private ManagedObject createRootTestMOInDPS(final DataBucket liveBucket, final DpsTestMoType testMOType, final String MOId) {
        final DpsTestMo dpsTestMO = DpsTestMoFactory.createTestMO(testMOType, MOId, MOId);
        final ManagedObject createdMO = liveBucket.getMibRootBuilder().namespace(dpsTestMO.getNamespace()).type(dpsTestMO.getType())
                .version(dpsTestMO.getVersion()).name(dpsTestMO.getId()).addAttributes(dpsTestMO.getAttributes()).create();
        logger.debug("Test Setup, created root MO: " + createdMO.getFdn() + " in DPS");
        return createdMO;
    }

    private ManagedObject createTestMOInDPS(final DataBucket liveBucket, final DpsTestMoType testMOType, final ManagedObject parentMO,
                                            final String MOId, final String nodeName) {
        final DpsTestMo dpsTestMO = DpsTestMoFactory.createTestMO(testMOType, MOId, nodeName);
        final ManagedObject createdMO = liveBucket.getMibRootBuilder().namespace(dpsTestMO.getNamespace()).type(dpsTestMO.getType())
                .version(dpsTestMO.getVersion()).name(dpsTestMO.getId()).addAttributes(dpsTestMO.getAttributes()).parent(parentMO).create();
        logger.debug("Test Setup, created MO: " + createdMO.getFdn() + " in DPS");
        return createdMO;
    }

    protected void deleteAllNodes() throws Exception {
        deleteAllNodes(DpsTestMo.TOP_NAMESPACE, "MeContext");
        /*
         * TODO EEITSIK, enable deletes below cannot currently cant do this as there is a bug in DPS which doesn't let us delete 'orphaned' MOs see
         * http://jira-oss.lmera.ericsson.se/browse/TORF-4622#comment-254860
         */
        // deleteAllNodes(CPP_NAMESPACE, "ManagedElement");
        // deleteAllNodes(ERBS_NAMESPACE, "ENodeBFunction");
    }

    protected void deleteAllNodes(final String namespace, final String type) throws Exception {
        userTransaction.begin();
        final QueryBuilder queryBuilder = dpsFacade.getQueryBuilder();
        final Query query = queryBuilder.createTypeQuery(namespace, type);
        final Iterator<PersistenceObject> iterator = dpsFacade.getLiveBucket().getQueryExecutor().execute(query);
        while (iterator.hasNext()) {
            final PersistenceObject po = iterator.next();
            dpsFacade.getLiveBucket().deletePo(po);
        }
        userTransaction.commit();
    }

}
