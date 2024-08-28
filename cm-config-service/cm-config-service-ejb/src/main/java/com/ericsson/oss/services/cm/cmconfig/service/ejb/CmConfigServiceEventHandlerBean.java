/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2013
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.services.cm.cmconfig.service.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.services.cm.cmconfig.service.event.handling.UpgradeEventObserver;

/**
 * Starts handling Service Framework events on startup of cm-config-service.
 */
@Singleton
@Startup
public class CmConfigServiceEventHandlerBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(CmConfigServiceEventHandlerBean.class);

    @SuppressWarnings("PMD.UnusedPrivateField")
    @Inject
    private UpgradeEventObserver upgradeEventObserver;

    /**
     * Indicates cm-config-service is ready to handle events.
     */
    @PostConstruct
    void startup() {
        LOGGER.info("CmConfigServiceEventHandlerBean started");
    }
}
