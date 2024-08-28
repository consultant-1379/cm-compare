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
package com.ericsson.oss.services.cm.cmconfig.service.event.handling;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static com.ericsson.oss.itpf.sdk.upgrade.UpgradePhase.SERVICE_CLUSTER_UPGRADE_FAILED;
import static com.ericsson.oss.itpf.sdk.upgrade.UpgradePhase.SERVICE_CLUSTER_UPGRADE_FINISHED_SUCCESSFULLY;
import static com.ericsson.oss.itpf.sdk.upgrade.UpgradePhase.SERVICE_CLUSTER_UPGRADE_PREPARE;
import static com.ericsson.oss.itpf.sdk.upgrade.UpgradePhase.SERVICE_INSTANCE_UPGRADE_FAILED;
import static com.ericsson.oss.itpf.sdk.upgrade.UpgradePhase.SERVICE_INSTANCE_UPGRADE_FINISHED_SUCCESSFULLY;
import static com.ericsson.oss.itpf.sdk.upgrade.UpgradePhase.SERVICE_INSTANCE_UPGRADE_PREPARE;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ericsson.oss.itpf.sdk.upgrade.UpgradeEvent;

/**
 * Unit tests for {@link UpgradeEventObserver}.
 */
@RunWith(MockitoJUnitRunner.class)
public class UpgradeEventObserverTest {
    private static final String EXPECTED_ACCEPT_RESPONSE = "OK";

    @InjectMocks
    private final UpgradeEventObserver objUnderTest = new UpgradeEventObserver();

    @Mock
    private UpgradeEvent upgradeEvent;

    /**
     * test upgradeEvent IsAccepted With UpgradePhase ServiceInstanceUpgradePrepare.
     */
    @Test
    public void upgradeEventIsAcceptedWithUpgradePhaseServiceInstanceUpgradePrepare() {
        when(this.upgradeEvent.getPhase()).thenReturn(SERVICE_INSTANCE_UPGRADE_PREPARE);
        this.objUnderTest.upgradeNotificationObserver(this.upgradeEvent);
        verify(this.upgradeEvent).accept(EXPECTED_ACCEPT_RESPONSE);
    }

    /**
     * test upgradeEvent IsAccepted With UpgradePhase ServiceClusterUpgradePrepare.
     */
    @Test
    public void upgradeEventIsAcceptedWithUpgradePhaseServiceClusterUpgradePrepare() {
        when(this.upgradeEvent.getPhase()).thenReturn(SERVICE_CLUSTER_UPGRADE_PREPARE);
        this.objUnderTest.upgradeNotificationObserver(this.upgradeEvent);
        verify(this.upgradeEvent).accept(EXPECTED_ACCEPT_RESPONSE);
    }

    /**
     * test upgradeEvent IsAcceptedWith UpgradePhase ServiceClusterUpgradeFailed.
     */
    @Test
    public void upgradeEventIsAcceptedWithUpgradePhaseServiceClusterUpgradeFailed() {
        when(this.upgradeEvent.getPhase()).thenReturn(SERVICE_CLUSTER_UPGRADE_FAILED);
        this.objUnderTest.upgradeNotificationObserver(this.upgradeEvent);
        verify(this.upgradeEvent).accept(EXPECTED_ACCEPT_RESPONSE);
    }

    /**
     * test upgradeEvent IsAcceptedWith UpgradePhaseService ClusterUpgradeFinishedSuccessfully.
     */
    @Test
    public void upgradeEventIsAcceptedWithUpgradePhaseServiceClusterUpgradeFinishedSuccessfully() {
        when(this.upgradeEvent.getPhase()).thenReturn(SERVICE_INSTANCE_UPGRADE_FINISHED_SUCCESSFULLY);
        this.objUnderTest.upgradeNotificationObserver(this.upgradeEvent);
        verify(this.upgradeEvent).accept(EXPECTED_ACCEPT_RESPONSE);
    }

    /**
     * test upgradeEvent IsAcceptedWith UpgradePhaseService InstanceUpgradeFailed.
     */
    @Test
    public void upgradeEventIsAcceptedWithUpgradePhaseServiceInstanceUpgradeFailed() {
        when(this.upgradeEvent.getPhase()).thenReturn(SERVICE_INSTANCE_UPGRADE_FAILED);
        this.objUnderTest.upgradeNotificationObserver(this.upgradeEvent);
        verify(this.upgradeEvent).accept(EXPECTED_ACCEPT_RESPONSE);
    }

    /**
     * test upgradeEvent IsAcceptedWith UpgradePhaseService InstanceUpgradeFinishedSuccessfully.
     */
    @Test
    public void upgradeEventIsAcceptedWithUpgradePhaseServiceInstanceUpgradeFinishedSuccessfully() {
        when(this.upgradeEvent.getPhase()).thenReturn(SERVICE_CLUSTER_UPGRADE_FINISHED_SUCCESSFULLY);
        this.objUnderTest.upgradeNotificationObserver(this.upgradeEvent);
        verify(this.upgradeEvent).accept(EXPECTED_ACCEPT_RESPONSE);
    }

}
