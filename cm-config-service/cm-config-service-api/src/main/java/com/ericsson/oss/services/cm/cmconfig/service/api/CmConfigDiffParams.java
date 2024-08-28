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

import java.io.Serializable;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

/**
 * Parameters for {@code diffConfig} API.
 *
 * @see CmConfigService#diffConfig
 */
public class CmConfigDiffParams implements Serializable {

    private static final long serialVersionUID = -8691782739633820356L;

    /**
     * Parameters for {@code diffConfig} API.
     */
    public enum CmConfigDiff {
        /**
         * Include ALL different NEs, not just intersecting between both configurations in the diff.
         */
        INLCUDE,

        /**
         * Diff will be limited to the set of NEs.
         */
        LIMIT;
    };

    /**
     * Detail Type per NE in the diff output.
     */
    public enum CmConfigDiffDetailType {

        /** Shows only NEs, no further detail on underlying MOs. */
        NE,

        /** Shows all changed attributes. */
        ATTRIBUTE
    }

    private Set<CmConfigDiff> cmDiffParams = EnumSet.noneOf(CmConfigDiff.class);
    private CmConfigDiffDetailType diffDetailType = CmConfigDiffDetailType.ATTRIBUTE;

    /**
     * Gets the cm config diff command parameters.
     *
     * @return the set of CmConfigDiff parameters
     */
    public Set<CmConfigDiff> getCmDiffParams() {
        return this.cmDiffParams;
    }

    /**
     * Sets the cm config diff command parameters.
     *
     * @param cmDiffParams
     *            the set of cmDiffParams
     */
    public void setCmDiffParams(final Set<CmConfigDiff> cmDiffParams) {
        this.cmDiffParams = cmDiffParams;
    }

    /**
     * Adds {@code cmConfigDiff} to the set.
     *
     * @param cmConfigDiff
     *            cmConfigDiff item
     */
    public void addCmDiffParam(final CmConfigDiff cmConfigDiff) {
        this.cmDiffParams.add(cmConfigDiff);
    }

    /**
     * Gets the diff detail type.
     *
     * @return the diffDetailType
     */
    public CmConfigDiffDetailType getDiffDetailType() {
        return this.diffDetailType;
    }

    /**
     * Sets the detail type. This will need to be set only if cmDiffParams has CmConfigDiff.DETAIL option selected.
     *
     * @param diffDetailType
     *            the diff detail type to set
     */
    public void setDiffDetailType(final CmConfigDiffDetailType diffDetailType) {
        this.diffDetailType = diffDetailType;
    }

    @Override
    public String toString() {
        return Arrays.toString(this.cmDiffParams.toArray());
    }

}
