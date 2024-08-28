/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2013
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.services.cm.cmparser.impl;

import com.ericsson.oss.services.cm.cmshared.dto.search.CmMatchCondition;

/**
 * 
 * Data container for object specification.
 * 
 */
public interface DataContainer {
    /**
     * Add a entry to the data container.
     * 
     * @param name
     *            of the element
     * @param value
     *            of the element
     * @param cmMatchCondition
     *            match condition
     */
    void addValue(String name, Object value, CmMatchCondition cmMatchCondition);

    /**
     * 
     * @return the data container.
     */
    Object getContainer();

}
