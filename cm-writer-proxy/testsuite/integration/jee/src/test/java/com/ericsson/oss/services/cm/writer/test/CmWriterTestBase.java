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

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;

/**
 * 
 * The base class for the CMEditorServiceBeanTest, injects the services to test as well as configures ear to be deployed on Jboss
 * 
 */

public class CmWriterTestBase extends DpsTestBase {

    @Deployment(name = "CMWriterServiceTestEar")
    public static Archive<?> createTestArchive() {
        final EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "CMWriterServiceTestEar.ear");
        Artifact.addEarRequiredlibraries(ear);
        ear.addAsModule(Artifact.createModuleArchive());
        ear.setManifest(Artifact.MANIFEST_MF_FILE);
        ear.addAsApplicationResource(Artifact.BEANS_XML_FILE);
        return ear;
    }

}