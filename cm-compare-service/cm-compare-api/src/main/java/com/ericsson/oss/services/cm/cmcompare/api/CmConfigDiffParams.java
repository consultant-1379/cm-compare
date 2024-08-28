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
package com.ericsson.oss.services.cm.cmcompare.api;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.Set;

/**
 * Parameter for diffConfig api.
 * 
 */
public class CmConfigDiffParams implements Serializable {

    private static final long serialVersionUID = -8691782739633820356L;

    /**
     * an enum to specify the param for diffConfig api.
     * 
     */
    public enum CmConfigDiff {
        deep, update, output, none
    };

    public Set<CmConfigDiff> cmDiffParams = EnumSet.noneOf(CmConfigDiff.class);

    private String updateConfigName;

    /**
     * @return the cmDiffParams
     */
    public Set<CmConfigDiff> getCmDiffParams() {
        return this.cmDiffParams;
    }

    /**
     * @param cmDiffParams
     *            the cmDiffParams to set
     */
    public void setCmDiffParams(final Set<CmConfigDiff> cmDiffParams) {
        this.cmDiffParams = cmDiffParams;
    }

    /**
     * @return the updateConfigName
     */
    public String getUpdateConfigName() {
        return this.updateConfigName;
    }

    /**
     * This should be called when CmConfigDiff.update is set.
     * 
     * @param updateConfigName
     *            the updateConfigName to set
     */
    public void setUpdateConfigName(final String updateConfigName) {
        this.updateConfigName = updateConfigName;
    }

    /**
     * Add {@code cmConfigDiff} to the set.
     * 
     * @param cmConfigDiff
     *            cmConfigDiff item
     */
    public void addCmDiffParam(final CmConfigDiff cmConfigDiff) {
        this.cmDiffParams.add(cmConfigDiff);
    }

}
