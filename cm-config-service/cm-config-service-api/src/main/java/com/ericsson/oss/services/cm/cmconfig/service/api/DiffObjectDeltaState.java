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
package com.ericsson.oss.services.cm.cmconfig.service.api;

/**
 * The state of an object in a target configuration, when compared to the state of the corresponding object in a reference configuration.
 */
public enum DiffObjectDeltaState {
    /**
     * The object exists in the target configuration, but not in the reference configuration.
     */
    CREATED,

    /**
     * The object does not exist in the target configuration, but does exist in the reference configuration.
     */
    DELETED,

    /**
     * The object exists in both configurations, but has one or more differing attribute values between the two configurations.
     */
    MODIFIED;
}
