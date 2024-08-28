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
package com.ericsson.oss.services.cm.cmparser.dto;

/**
 * 
 * This class represents optional options for the cli diff config command.
 * 
 */
public class DiffConfigOptions {
    private boolean diffConfigDeepOption;
    private boolean diffConfigImportOption;
    private boolean diffConfigUpdateOption;
    private String diffConfigUpdateName;

    /**
     * @return the diffConfigDeepOption
     */
    public boolean isDiffConfigDeepOption() {
        return this.diffConfigDeepOption;
    }

    /**
     * @param diffConfigDeepOption
     *            the diffConfigDeepOption to set
     */
    public void setDiffConfigDeepOption(final boolean diffConfigDeepOption) {
        this.diffConfigDeepOption = diffConfigDeepOption;
    }

    /**
     * @return the diffConfigImportOption
     */
    public boolean isDiffConfigImportOption() {
        return this.diffConfigImportOption;
    }

    /**
     * @param diffConfigImportOption
     *            the diffConfigImportOption to set
     */
    public void setDiffConfigImportOption(final boolean diffConfigImportOption) {
        this.diffConfigImportOption = diffConfigImportOption;
    }

    /**
     * @return the diffConfigUpdateOption
     */
    public boolean isDiffConfigUpdateOption() {
        return this.diffConfigUpdateOption;
    }

    /**
     * @param diffConfigUpdateOption
     *            the diffConfigUpdateOption to set
     */
    public void setDiffConfigUpdateOption(final boolean diffConfigUpdateOption) {
        this.diffConfigUpdateOption = diffConfigUpdateOption;
    }

    /**
     * @return the diffConfigUpdateName
     */
    public String getDiffConfigUpdateName() {
        return this.diffConfigUpdateName;
    }

    /**
     * @param diffConfigUpdateName
     *            the diffConfigUpdateName to set
     */
    public void setDiffConfigUpdateName(final String diffConfigUpdateName) {
        this.diffConfigUpdateName = diffConfigUpdateName;
    }

}
