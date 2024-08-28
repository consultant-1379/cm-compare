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

import java.io.File;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;

/**
 * Maven artifact constants and setup configuration for tests.
 */
public final class Artifact {
    static final File MANIFEST_MF_FILE = new File("src/test/resources/META-INF/MANIFEST.MF");
    static final File BEANS_XML_FILE = new File("src/test/resources/META-INF/beans.xml");
    private static final String GROUP_ID_SFWK = "com.ericsson.oss.itpf.sdk";
    private static final String GROUP_ID_CM_SERVICES = "com.ericsson.oss.services.cm";
    private static final String MAVEN_DEPENDENCY_RESOLVER = "org.jboss.shrinkwrap.resolver:shrinkwrap-resolver-api-maven";

    private static final MavenDependencyResolver resolver = DependencyResolvers.use(MavenDependencyResolver.class).loadMetadataFromPom("pom.xml");

    private Artifact() {
    }

    /**
     * Adds the dependent libraries to an Enterprise Archive (EAR).
     * 
     * @param archive
     *            Enterprise Archive
     */
    static void addEarRequiredlibraries(final EnterpriseArchive archive) {
        archive.addAsLibraries(resolveAsFiles(GROUP_ID_SFWK, "service-framework-dist"));
        archive.addAsLibraries(resolveAsFiles(GROUP_ID_CM_SERVICES, "cm-config-service-api"));
        archive.addAsLibraries(resolveAsFiles(GROUP_ID_CM_SERVICES, "cm-common-api"));
        archive.addAsLibraries(resolveAsFiles(GROUP_ID_CM_SERVICES, "cm-common-modelservice-extensions"));
        archive.addAsLibraries(resolveAsFiles(GROUP_ID_CM_SERVICES, "cm-common-testutil"));
        archive.addAsLibraries(resolveAsFiles("org.jboss.spec.javax.batch", "jboss-batch-api_1.0_spec"));
        archive.addAsLibraries(resolver.artifact(MAVEN_DEPENDENCY_RESOLVER).resolveAsFiles());
    }

    /**
     * Creates the jar containing test artifacts.
     * 
     * @return JavaArchive
     */
    static Archive<?> createModuleArchive() {
        final JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "cm-config-service-test-bean-lib.jar")
                .addAsResource("META-INF/beans.xml", "META-INF/beans.xml").addAsResource(MANIFEST_MF_FILE).addClass(CmConfigServiceProxyBean.class)
                .addPackage(Artifact.class.getPackage());
        return archive;
    }

    private static File[] resolveAsFiles(final String groupId, final String artifactId) {
        return resolver.artifact(groupId + ":" + artifactId + ":jar").resolveAsFiles();
    }

}
