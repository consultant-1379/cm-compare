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
import java.util.Collection;

/**
 * Result of Diff to be sent back to the cm-config-handler.
 */
public class DiffResult implements Serializable {
    private static final long serialVersionUID = -782360978876341090L;
    private String moName;
    private String fdn;
    private DiffObjectDeltaState moState;
    private Collection<DiffAttributeDelta> attributeDeltas;

    /**
     * Gets the name of the managed object.
     *
     * @return the managed object name
     */
    public String getMoName() {
        return this.moName;
    }

    /**
     * Sets the name of the managed object.
     *
     * @param moName
     *            the managed object name to set
     */
    public void setMoName(final String moName) {
        this.moName = moName;
    }

    /**
     * Gets the FDN.
     *
     * @return the FDN
     */
    public String getFdn() {
        return this.fdn;
    }

    /**
     * Sets the FDN.
     *
     * @param fdn
     *            the FDN to set
     */
    public void setFdn(final String fdn) {
        this.fdn = fdn;
    }

    /**
     * Gets the delta state {@link DiffObjectDeltaState} of the persistent object in the target configuration relative to the reference configuration.
     *
     * @return the managed object delta state
     */
    public DiffObjectDeltaState getMoState() {
        return this.moState;
    }

    /**
     * Sets the delta state of the managed object delta state in the target configuration relative to the reference configuration.
     *
     * @param moState
     *            the managed object delta state to set
     */
    public void setMoState(final DiffObjectDeltaState moState) {
        this.moState = moState;
    }

    /**
     * Gets the attribute deltas showing the differences in attribute values for the persistence object across the two configurations.
     * <p>
     * The members of the list will consist of the following, depending on the delta state of the persistence object:
     * <ul>
     * <li>{@link ObjectDeltaState#CREATED}: one for each attribute that has been set on the object in the target configuration</li>
     * <li>{@link ObjectDeltaState#MODIFIED}: one for each attribute where the value differs on the instances in the two configurations</li>
     * <li>{@link ObjectDeltaState#DELETED}: one for each attribute that has been set on the object in the reference configuration</li>
     * </ul>
     *
     * @return the attribute deltas
     */
    public Collection<DiffAttributeDelta> getAttributeDeltas() {
        return this.attributeDeltas;
    }

    /**
     * Sets the attribute deltas.
     *
     * @param attributeDeltas
     *            the attribute deltas to set
     */
    public void setAttributeDeltas(final Collection<DiffAttributeDelta> attributeDeltas) {
        this.attributeDeltas = attributeDeltas;
    }

}
