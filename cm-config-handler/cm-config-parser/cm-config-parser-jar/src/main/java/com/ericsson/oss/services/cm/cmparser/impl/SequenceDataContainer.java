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

import java.util.ArrayList;
import java.util.List;

import com.ericsson.oss.services.cm.cmshared.dto.search.CmMatchCondition;

/**
 * 
 * {@link DataContainer} implementation for lists or sequences.
 * 
 */
public class SequenceDataContainer implements DataContainer {

    private final List<Object> objects = new ArrayList<>();

    @Override
    public void addValue(final String name, final Object value, final CmMatchCondition cmMatchCondition) {
        // Name is ignored for Lists
        // CmMatchCondition not supported for list values
        this.objects.add(value);
    }

    @Override
    public Object getContainer() {
        return this.objects;
    }

}
