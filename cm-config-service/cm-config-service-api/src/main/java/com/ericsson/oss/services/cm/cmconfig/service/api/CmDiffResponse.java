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

import java.util.ArrayList;
import java.util.Collection;

import com.ericsson.oss.services.cm.cmshared.dto.AbstractCmResponse;

/**
 * CmDiffResponse used for returning diff of two configurations.
 */
public class CmDiffResponse extends AbstractCmResponse {
    private static final long serialVersionUID = -4532002243145782002L;
    private Collection<DiffResult> diffResults = new ArrayList<>(0);

    /**
     * Gets the list of diff results.
     *
     * @return the list of diff results
     */
    public Collection<DiffResult> getDiffResults() {
        return this.diffResults;
    }

    /**
     * Sets the list of diff results.
     *
     * @param diffResult
     *            the list of diff results to set
     */
    public void setDiffResults(final Collection<DiffResult> diffResult) {
        this.diffResults = diffResult;
    }

}
