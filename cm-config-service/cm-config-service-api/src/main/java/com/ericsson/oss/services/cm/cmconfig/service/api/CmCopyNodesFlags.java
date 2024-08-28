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
 * Specifies the flags for the node copy command.
 */
public class CmCopyNodesFlags implements Serializable {

    private static final long serialVersionUID = 1951497621826873661L;

    /**
     * The allowed flags.
     */
    public enum CmConfigCopy {
        /**
         * This will overwrite a node in the target configuration if it already exists.
         */
        OVERWRITE
    }

    private Set<CmConfigCopy> cmCopyFlags;

    /**
     * Constructor with no flags set.
     */
    public CmCopyNodesFlags() {
        this.cmCopyFlags = EnumSet.noneOf(CmConfigCopy.class);
    }

    /**
     * Constructor using the specified flags.
     *
     * @param flags
     *            the flags to set
     */
    public CmCopyNodesFlags(final EnumSet<CmConfigCopy> flags) {
        this.cmCopyFlags = EnumSet.copyOf(flags);
    }

    /**
     * Gets the copy flags.
     *
     * @return the flags.
     */
    public Set<CmConfigCopy> getCopyFlags() {
        return this.cmCopyFlags;
    }

    /**
     * Sets the copy flags.
     *
     * @param cmCopyFlags
     *            the flags to set.
     */
    public void setCmCopyFlags(final Set<CmConfigCopy> cmCopyFlags) {
        this.cmCopyFlags = cmCopyFlags;
    }

    @Override
    public String toString() {
        return Arrays.toString(this.cmCopyFlags.toArray());
    }

}
