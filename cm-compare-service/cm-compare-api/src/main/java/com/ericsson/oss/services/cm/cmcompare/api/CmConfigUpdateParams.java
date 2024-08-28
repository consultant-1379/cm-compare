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
 * CmConfigUpdateParams specifies the parameters that can be specified with the
 * updateConfig api.
 */
public class CmConfigUpdateParams implements Serializable {

    private static final long serialVersionUID = -6319753642287156004L;

    /**
     * an enum to specify the update config param.
     * 
     */
    public enum CmConfigUpdate {
        create, delete, overwrite, none
    };

    private Set<CmConfigUpdate> cmUpdateParams = EnumSet.noneOf(CmConfigUpdate.class);

    public Set<CmConfigUpdate> getCmUpdateParams() {
        return this.cmUpdateParams;
    }

    public void setCmUpdateParams(final Set<CmConfigUpdate> cmUpdateParams) {
        this.cmUpdateParams = cmUpdateParams;
    }

    /**
     * Add {@code cmConfigUpdate} to the set.
     * 
     * @param cmConfigUpdate
     *            cmConfigUpdate item
     */
    public void addCmUpdateParam(final CmConfigUpdate cmConfigUpdate) {
        this.cmUpdateParams.add(cmConfigUpdate);
    }

}
