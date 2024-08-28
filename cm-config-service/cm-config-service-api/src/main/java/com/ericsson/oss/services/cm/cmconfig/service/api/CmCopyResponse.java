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
import java.util.List;

import com.ericsson.oss.services.cm.cmshared.dto.AbstractCmResponse;

/**
 * Transfer object for response from copy command.
 */
public class CmCopyResponse extends AbstractCmResponse {
    private static final long serialVersionUID = -4532002243145782001L;
    private List<CopyResult> copyResults = new ArrayList<>(0);

    public List<CopyResult> getCopyResults() {
        return this.copyResults;
    }

    public void setCopyResults(final List<CopyResult> copyResults) {
        this.copyResults = copyResults;
    }
}
