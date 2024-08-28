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
 * This class represents optional options for the cli list configs command.
 * 
 */
public class ListConfigOptions {
    private boolean listConfigAllOption;
    private boolean listConfigOwnerOption;
    private String configOwner;

    /**
     * @return the listConfigAllOption
     */
    public boolean isListConfigAllOption() {
        return this.listConfigAllOption;
    }

    /**
     * @param listConfigAllOption
     *            the listConfigAllOption to set
     */
    public void setListConfigAllOption(final boolean listConfigAllOption) {
        this.listConfigAllOption = listConfigAllOption;
    }

    /**
     * @return the listConfigOwnerOption
     */
    public boolean isListConfigOwnerOption() {
        return this.listConfigOwnerOption;
    }

    /**
     * @param listConfigOwnerOption
     *            the listConfigOwnerOption to set
     */
    public void setListConfigOwnerOption(final boolean listConfigOwnerOption) {
        this.listConfigOwnerOption = listConfigOwnerOption;
    }

    /**
     * @return the configOwner
     */
    public String getConfigOwner() {
        return this.configOwner;
    }

    /**
     * @param configOwner
     *            the configOwner to set
     */
    public void setConfigOwner(final String configOwner) {
        this.configOwner = configOwner;
    }

}
