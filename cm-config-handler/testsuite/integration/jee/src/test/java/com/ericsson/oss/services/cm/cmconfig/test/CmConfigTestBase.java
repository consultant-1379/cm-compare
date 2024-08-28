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
package com.ericsson.oss.services.cm.cmconfig.test;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.junit.After;
import org.junit.Before;

import com.ericsson.oss.services.cm.testutil.DataPersistenceServiceProxy;
import com.ericsson.oss.services.cm.testutil.TestDataBuilderImpl;
import com.ericsson.oss.services.scriptengine.spi.CommandHandler;

/**
 * 
 * The base class for the CMEditorServiceBeanTest, injects the services to test
 * as well as configures ear to be deployed on Jboss
 * 
 */

public class CmConfigTestBase extends TestDataBuilderImpl {

    CommandHandler cmConfigService;

    @Inject
    CmConfigServiceProxy cmConfigurationServiceProxy;

    @Inject
    DataPersistenceServiceProxy dataPersistenceServiceProxy;

    @Deployment(name = "CMEditorServiceTestBean")
    public static Archive<?> createTestArchive() {

        final EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class);

        Artifact.addEarRequiredlibraries(ear);
        ear.addAsModule(Artifact.createModuleArchive());
        ear.setManifest(Artifact.MANIFEST_MF_FILE);
        ear.addAsApplicationResource(Artifact.BEANS_XML_FILE);
        return ear;
    }

    @Before
    public void init() throws Exception {
        cmConfigService = cmConfigurationServiceProxy.getCmConfigurationService();
        setDataPersistenceService(dataPersistenceServiceProxy.getDataPersistenceService());
        createTestData();
    }

    @After
    public void cleanup() throws Exception {
        removeTestData();
    }

}