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

import java.io.File;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;

/**
 * Maven artifact constants and setup configuration for tests
 */
public class Artifact {
    static final File MANIFEST_MF_FILE = new File("src/test/resources/META-INF/MANIFEST.MF");
    static final File BEANS_XML_FILE = new File("src/test/resources/META-INF/beans.xml");
    private static final String GROUP_ID_SFWK = "com.ericsson.oss.itpf.sdk";
    private static final String GROUP_ID_CM_SERVICES = "com.ericsson.oss.services.cm";

    private final static MavenDependencyResolver resolver = DependencyResolvers.use(MavenDependencyResolver.class).loadMetadataFromPom("pom.xml");

    static Archive<?> createModuleArchive() {
        final JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "cm-writer-test-bean-lib.jar")
                .addAsResource("META-INF/beans.xml", "META-INF/beans.xml").addAsResource(MANIFEST_MF_FILE).addClass(ArquillianProxyBean.class)
                .addPackage(Artifact.class.getPackage());
        return archive;
    }

    static void addEarRequiredlibraries(final EnterpriseArchive archive) {
        archive.addAsLibraries(resolveAsFiles(GROUP_ID_SFWK, "sdk-config-core"));
        archive.addAsLibraries(resolveAsFiles(GROUP_ID_SFWK, "sdk-cluster-core"));
        archive.addAsLibraries(resolveAsFiles(GROUP_ID_SFWK, "sdk-recording"));
        archive.addAsLibraries(resolveAsFiles(GROUP_ID_CM_SERVICES, "cm-writer-api"));
        archive.addAsLibraries(resolveAsFiles(GROUP_ID_CM_SERVICES, "cm-common-tools"));
        archive.addAsLibraries(resolveAsFiles(GROUP_ID_CM_SERVICES, "cm-common-testutil"));
    }

    private static File[] resolveAsFiles(final String groupId, final String artifactId) {
        return resolver.artifact(groupId + ":" + artifactId + ":jar").resolveAsFiles();
    }
}
