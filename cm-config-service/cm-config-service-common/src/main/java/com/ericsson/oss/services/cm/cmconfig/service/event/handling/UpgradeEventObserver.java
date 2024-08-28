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
package com.ericsson.oss.services.cm.cmconfig.service.event.handling;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import com.ericsson.oss.itpf.sdk.upgrade.UpgradeEvent;

/**
 * Handles {@link UpgradeEvent} from Service Framework.
 */
@ApplicationScoped
public class UpgradeEventObserver {

    /**
     * Observes and responds to {@link UpgradeEvent}.
     *
     * @param event
     *            UpgradeEvent
     */
    public void upgradeNotificationObserver(@Observes final UpgradeEvent event) {
        // At the moment we are accepting with "OK" for all UpgradePhases (phase can be checked with event.getPhase())
        // if different handling is required for certain UpgradePhases it should be added below
        event.accept("OK");
    }
}
