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
package com.ericsson.oss.services.cm.cmconfig.command;

/**
 * 
 * Create a {@link CmConfigCommandObject} based on the string representation of
 * the command.
 * 
 */
public interface CmConfigCommandFactory {
    /**
     * Create a {@link CmConfigCommandObject}.
     * 
     * @param cmd
     *            String representation of the cm-config command.
     * @return CmConfigCommandObject
     */
    CmConfigCommandObject createCommandModel(String cmd);

}
