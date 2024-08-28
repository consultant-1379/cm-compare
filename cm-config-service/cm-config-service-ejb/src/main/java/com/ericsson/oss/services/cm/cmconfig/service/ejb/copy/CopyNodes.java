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
package com.ericsson.oss.services.cm.cmconfig.service.ejb.copy;

import javax.ejb.Local;

import com.ericsson.oss.services.cm.cmconfig.service.api.CmCopyNodesFlags;
import com.ericsson.oss.services.cm.cmconfig.service.api.CmCopyResponse;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchCriteria;

/**
 * Provides the ability to copy nodes from one configuration to another.
 */
@Local
public interface CopyNodes {

    /**
     * Copies specified nodes from a source to target configuration.
     *
     * @param sourceConfigName
     *            name of the source configuration
     * @param targetConfigName
     *            name of the target configuration
     * @param nodesScope
     *            node scope; the search criteria which specifies the nodes to
     *            copy.
     * @param copyFlags
     *            flags which specify the behavior of the copy.
     * @return CmCopyResponse
     */
    CmCopyResponse copyNodes(final String sourceConfigName, final String targetConfigName, final CmSearchCriteria nodesScope,
            final CmCopyNodesFlags copyFlags);

}
