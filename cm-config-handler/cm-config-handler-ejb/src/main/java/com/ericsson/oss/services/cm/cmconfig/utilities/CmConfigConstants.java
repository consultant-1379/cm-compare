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
package com.ericsson.oss.services.cm.cmconfig.utilities;

/**
 * 
 * Definition of common constants like status code response and common terms.
 * 
 */
public final class CmConfigConstants {
    public static final String ME_CONTEXT = "MeContext";
    public static final String OSS_TOP = "OSS_TOP";
    public static final String OSS_TOP_VERSION = "1.0.1";

    /** COMMON TERMS. **/
    public static final String FDN = "FDN";
    public static final String TYPE = "TYPE";
    public static final String NAME = "NAME";
    public static final String NAMESPACE = "NAMESPACE";
    public static final String NAMESPACEVERSION = "NAMESPACEVERSION";
    public static final String EAI = "Entity Address Information";

    /** STRINGS FOR STATUS CODE RESPONSE. **/
    public static final String SUCCESS_MESSAGE = "SUCCESS";
    public static final String FAILURE_MESSAGE = "FAILURE";
    public static final String INVALID_PARENT_DESCRIPTION = "Invalid Parent Child Relationship";
    public static final String INVALID_ROOT_DESCRIPTION = "Invalid Root";
    public static final String NO_FDN_RESOURCE_DESCRIPTION = "No Resource For Given FDN";
    public static final String UNKNOWN_DESCRIPTION = "Unknown Failure";
    public static final String SUCCESS_DESCRIPTION = "Success";

    private CmConfigConstants() {

    }

}
