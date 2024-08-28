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
package com.ericsson.oss.services.cm.config.service.test;

import static com.ericsson.oss.services.cm.cmshared.dto.CmConstants.LIVE_CONFIGURATION;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.slf4j.Logger;

import com.ericsson.oss.itpf.datalayer.dps.DataBucket;
import com.ericsson.oss.itpf.datalayer.dps.DataPersistenceService;
import com.ericsson.oss.itpf.datalayer.dps.persistence.ManagedObject;
import com.ericsson.oss.itpf.datalayer.dps.persistence.PersistenceObject;
import com.ericsson.oss.itpf.datalayer.dps.query.ContainmentRestrictionBuilder;
import com.ericsson.oss.itpf.datalayer.dps.query.Query;
import com.ericsson.oss.itpf.datalayer.dps.query.QueryBuilder;
import com.ericsson.oss.itpf.datalayer.dps.query.QueryExecutor;
import com.ericsson.oss.itpf.datalayer.dps.query.RestrictionBuilder;
import com.ericsson.oss.itpf.datalayer.dps.query.TypeRestrictionBuilder;
import com.ericsson.oss.services.cm.cmconfig.service.api.CmConfigService;
import com.ericsson.oss.services.cm.cmshared.dto.AbstractCmResponse;
import com.ericsson.oss.services.cm.testutil.DataPersistenceServiceProxy;
import com.ericsson.oss.services.cm.testutil.TestDataBuilder;
import com.ericsson.oss.services.cm.testutil.mo.MeContext;

/**
 * The base class for the CmConfigServiceBasicIT, injects the services to test
 * as well as configures EAR to be deployed on JBoss.
 */
@SuppressWarnings("PMD.SignatureDeclareThrowsException")
public abstract class CmConfigServiceTestBase {

    public static final String[] configs = { "Configuration1", "Configuration2", LIVE_CONFIGURATION };

    @Inject
    protected Logger logger;

    CmConfigService cmConfigService;

    @Inject
    CmConfigTestBuilder testBuilder;

    @Inject
    CmConfigServiceProxy cmConfigServiceProxy;

    @Inject
    DataPersistenceServiceProxy dataPersistenceServiceProxy;

    /**
     * Creates a test enterprise archive (EAR).
     *
     * @return enterprise archive
     */
    @Deployment(name = "CMConfigServiceTestEar")
    public static Archive<?> createTestArchive() {
        final EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "CMConfigServiceTestEar.ear");
        Artifact.addEarRequiredlibraries(ear);
        ear.addAsModule(Artifact.createModuleArchive());
        ear.setManifest(Artifact.MANIFEST_MF_FILE);
        ear.addAsApplicationResource(Artifact.BEANS_XML_FILE);
        return ear;
    }

    /**
     * Initializes the test setup for running the individual tests.
     *
     * @throws Exception
     *             due to DPS or cmConfigService exceptions.
     */
    @Before
    public void init() throws Exception {
        this.logger.info("start init");
        final DataPersistenceService dps = this.dataPersistenceServiceProxy.getDataPersistenceService();
        this.cmConfigService = this.cmConfigServiceProxy.getCmConfigService();
        this.testBuilder.setDataPersistenceService(dps);
        deleteAllConfigBuckets();
        setupTest();
        this.logger.info("end init");
    }

    protected abstract void setupTest() throws Exception;

    /**
     * Cleans up after running each test.
     *
     * @throws Exception
     *             due to DPS exceptions
     */
    @After
    public void cleanup() throws Exception {
        this.logger.info("start cleanup");
        deleteAllConfigBuckets();
        this.testBuilder.removeTestData(TestDataBuilder.CONFIGURATION_FOR_TEST);
        this.logger.info("end cleanup");
    }

    /**
     * Deletes all config buckets - cleanup.
     *
     * @throws Exception
     *             due to DPS exceptions
     */
    protected void deleteAllConfigBuckets() throws Exception {
        final DataPersistenceService dps = this.dataPersistenceServiceProxy.getDataPersistenceService();
        this.testBuilder.userTransaction.begin();
        final Collection<String> configBuckets = dps.getAllDataBucketNames();
        final Iterator<String> configNames = configBuckets.iterator();
        while (configNames.hasNext()) {
            final String bucketName = configNames.next();
            final DataBucket bucket = dps.getDataBucket(bucketName);
            if (!bucket.isLiveBucket()) {
                dps.deleteDataBucket(bucketName, true);
            }
        }
        this.testBuilder.userTransaction.commit();
    }

    /**
     * Gets all MeContext in the configuration. It is a type query per MeContext.
     *
     * @param config
     *            the config.
     * @return the list of ManagedObject that exists in the configuration
     * @throws Exception
     *             due to DPS exception
     */
    protected List<ManagedObject> getAllMeContextMos(final DataBucket config) throws Exception {
        final DataPersistenceService dps = this.dataPersistenceServiceProxy.getDataPersistenceService();
        final QueryBuilder queryBuilder = dps.getQueryBuilder();
        final Query<TypeRestrictionBuilder> typeQuery = queryBuilder.createTypeQuery(TestDataBuilder.TOP_NAMESPACE, MeContext.ME_CONTEXT_MO_NAME);
        final List<ManagedObject> managedObjects = executeQuery(config, typeQuery);

        return managedObjects;
    }

    /**
     * Queries the ManagedObject hierarchy of a base MO FDN.
     *
     * @param config
     *            the configuration name
     * @param baseMoFdn
     *            the base mod FDN
     * @return the full tree of ManagedObjects
     * @throws Exception
     *             due to DPS exception
     */
    protected List<ManagedObject> getMoHierarchy(final DataBucket config, final String baseMoFdn) throws Exception {
        final DataPersistenceService dps = this.dataPersistenceServiceProxy.getDataPersistenceService();
        final QueryBuilder queryBuilder = dps.getQueryBuilder();
        final Query<ContainmentRestrictionBuilder> query = queryBuilder.createContainmentQuery(baseMoFdn);
        final List<ManagedObject> managedObjects = executeQuery(config, query);
        return managedObjects;
    }

    protected void assertResponse(final AbstractCmResponse expected, final AbstractCmResponse actual) {
        this.logger.debug("Asserting response of type" + actual.getClass());

        // Assert status code
        this.logger.info(MessageFormat.format("status code: expected {0}, received {1}", expected.getStatusCode(), actual.getStatusCode()));
        Assert.assertTrue("Status code not as expected ", actual.getStatusCode() == expected.getStatusCode());
        // Assert status message
        if (!(expected.getStatusMessage() == null)) {
            this.logger.info(MessageFormat.format("status msg: expected {0}, received {1}", expected.getStatusMessage(), actual.getStatusMessage()));

            Assert.assertThat(actual.getStatusMessage().toLowerCase(), CoreMatchers.containsString(expected.getStatusMessage().toLowerCase()));
            Assert.assertThat(actual.getStatusMessage().toLowerCase(), Matchers.not(Matchers.containsString("exception")));
            Assert.assertThat(actual.getStatusMessage().toLowerCase(), Matchers.not(Matchers.containsString("bucket")));
        }
        // Assert error code
        this.logger.info(MessageFormat.format("error code: expected {0}, received {1}", expected.getErrorCode(), actual.getErrorCode()));
        Assert.assertTrue("Error code not as expected", expected.getErrorCode() == actual.getErrorCode());
    }

    private List<ManagedObject> executeQuery(final DataBucket config, final Query<? extends RestrictionBuilder> query) {
        final QueryExecutor queryExecutor = config.getQueryExecutor();
        final Iterator<PersistenceObject> poIterator = queryExecutor.execute(query);
        final List<ManagedObject> managedObjects = new ArrayList<>();
        while (poIterator.hasNext()) {
            final PersistenceObject persistenceObject = poIterator.next();
            managedObjects.add((ManagedObject) persistenceObject);
        }
        return managedObjects;
    }

}
