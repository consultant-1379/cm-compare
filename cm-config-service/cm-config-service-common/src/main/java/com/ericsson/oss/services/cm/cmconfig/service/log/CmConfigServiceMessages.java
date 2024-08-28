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
package com.ericsson.oss.services.cm.cmconfig.service.log;

import java.io.Serializable;

/**
 * Config service messages for <em>status codes</em> in {@code CmResponse}.
 *
 * @see com.ericsson.oss.services.cm.cmshared.dto.CmResponse
 */
public final class CmConfigServiceMessages implements Serializable {
    /** Create configuration message. */
    public static final String CREATE_CONFIG = "createConfig";

    /** List configuration message. */
    public static final String LIST_CONFIG = "listConfig";

    /** Delete configuration message. */
    public static final String DELETE_CONFIG = "deleteConfig";

    /** Copy nodes message. */
    public static final String COPY_NODES = "copyNodes";

    /** Diff configuration message. */
    public static final String DIFF_CONFIG = "diffConfig";

    /** Create configuration success message. */
    public static final String CONFIG_CREATE_SUCCESS_MESSAGE = "Config Successfully Created";

    /** Delete configuration success message. */
    public static final String CONFIG_DELETE_SUCCESS_MESSAGE = "Config Successfully Deleted";

    /** Configuration already exists error message. */
    public static final String CONFIGURATION_ALREADY_EXISTS_ERROR = "Configuration already exists";

    /** Configuration does not exist error. */
    public static final String CONFIGURATION_DOES_NOT_EXIST_ERROR = "Configuration does not exist";

    /** Configuration does not exist error. */
    public static final String CONFIGURATION_PARAM_DOES_NOT_EXIST_ERROR = "The configuration {0} does not exist";

    /** Delete live configuration error. */
    public static final String DELETE_LIVE_CONFIGURATION_ERROR = "Cannot DELETE Live configuration";

    /** Delete non-empty configuration error. */
    public static final String DELETE_NON_EMPTY_CONFIGURATION_ERROR = "Cannot DELETE non-empty configuration without force option";

    /** Copy nodes success message. */
    public static final String CONFIG_COPY_COMPLETED_MESSAGE = "Copy process completed";

    /** No nodes were specified message. */
    public static final String CONFIG_COPY_NODES_NOT_SPECIFIED_MESSAGE = "No Nodes were specified";

    /** The scope specified is not correct message. */
    public static final String CONFIG_COPY_NODES_SCOPE_NOT_CORRECT = "The scope specified is not correct";

    /** Node Not copied because it already exists in target configuration message. */
    public static final String CONFIG_COPY_NODE_EXIST_IN_TARGET = "Node Not copied because It already exists in the target configuration";

    /** Node Not copied because it does not exist in source configuration message. */
    public static final String CONFIG_COPY_NODE_DOES_NOT_EXIST_IN_SOURCE = "Node Not copied because it does not exist in the source configuration";

    /** Node overwritten message. */
    public static final String CONFIG_COPY_NODE_OVERWRITTEN = "Node Overwritten";

    /** Node copied message. */
    public static final String CONFIG_COPY_NODE_COPIED = "Node Copied";

    /** Diff configs success message. */
    public static final String CONFIG_DIFF_SUCCESS_MESSAGE = "Config Diff Completed";

    /** The configuration is not a non-live config message. */
    public static final String CONFIG_PARAM_IS_NOT_NON_LIVE_ERROR = "The target configuration {0} must be a non-live configuration";

    /** The supplied managed object is not a top root message. */
    public static final String MANAGED_OBJECT_IS_NOT_TOP_ROOT_ERROR = "The supplied object {0} is not a top root";

    /** The supplied managed object already exists in the target configuration message. */
    public static final String MANAGED_OBJECT_ALREADY_EXISTS = "The supplied object {0} already exists in the target configuration";

    /** The supplied managed object has been deleted or the configuration is read only message. */
    public static final String MANAGED_OBJECT_DELETED_OR_READ_ONLY_BUCKET = "The supplied object {0} has been deleted or the config is read only";

    /** Error during copy process of the supplied managed object message. */
    public static final String MANAGED_OBJECT_COPY_ERROR = "Error during copy process of the supplied object {0}";

    /** The diff param is invalid. */
    public static final String DIFF_PARAM_IS_INVALID = "Invalid diff param";

    /** The diff search criteria is invalid. */
    public static final String DIFF_SEARCH_CRITERIA_IS_INVALID = "Invalid diff search criteria";

    /** Solution for diff config is invalid. */
    public static final String SOLUTION_FOR_CONFIG_INVALID = "Specify a valid configuration";

    /** Solution for config already exists. */
    public static final String SOLUTION_FOR_CONFIG_ALREADY_EXISTS = "Specify another configuration name";

    /** Solution for Invalid Managed Object. */
    public static final String SOLUTION_FOR_MANAGED_OBJECT_ERROR = "Specify a valid managed object";

    /** Solution for Invalid scope. */
    public static final String SOLUTION_FOR_INVALID_SCOPE = "Specify a valid scope";

    /** Solution for Invalid nodes to copy. */
    public static final String SOLUTION_FOR_CONFIG_COPY_NODES_NOT_SPECIFIED = "Specify nodes to copy";

    /** Solution for Invalid diff param. */
    public static final String SOLUTION_FOR_DIFF_PARAM_IS_INVALID = "Specify a valid diff param";

    /** Solution for delete non-empty configuration error. */
    public static final String SOLUTION_FOR_DELETE_NON_EMPTY_CONFIGURATION_ERROR = "Specify an empty configuration";

    /** Solution for delete live configuration error. */
    public static final String SOLUTION_FOR_DELETE_LIVE_CONFIGURATION_ERROR = "Specify a non Live configuration";

    /** Solution for all other exceptions. */
    public static final String SOLUTION_FOR_GENERIC_EXCEPTION = " ";

    private static final long serialVersionUID = -2779552538537888088L;

    private CmConfigServiceMessages() {
    }
}
