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
 * This class represents optional options for the cli update config command.
 * 
 */
public class UpdateConfigOptions {

    private boolean updateConfigCreateOption;
    private boolean updateConfigDeleteOption;
    private boolean updateConfigOverwriteOption;

    /**
     * @return the updateConfigCreateOption
     */
    public boolean isUpdateConfigCreateOption() {
        return this.updateConfigCreateOption;
    }

    /**
     * @param updateConfigCreateOption
     *            the updateConfigCreateOption to set
     */
    public void setUpdateConfigCreateOption(final boolean updateConfigCreateOption) {
        this.updateConfigCreateOption = updateConfigCreateOption;
    }

    /**
     * @return the updateConfigDeleteOption
     */
    public boolean isUpdateConfigDeleteOption() {
        return this.updateConfigDeleteOption;
    }

    /**
     * @param updateConfigDeleteOption
     *            the updateConfigDeleteOption to set
     */
    public void setUpdateConfigDeleteOption(final boolean updateConfigDeleteOption) {
        this.updateConfigDeleteOption = updateConfigDeleteOption;
    }

    /**
     * @return the updateConfigOverwriteOption
     */
    public boolean isUpdateConfigOverwriteOption() {
        return this.updateConfigOverwriteOption;
    }

    /**
     * @param updateConfigOverwriteOption
     *            the updateConfigOverwriteOption to set
     */
    public void setUpdateConfigOverwriteOption(final boolean updateConfigOverwriteOption) {
        this.updateConfigOverwriteOption = updateConfigOverwriteOption;
    }

}
