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

/**
 * Diff of Attributes.
 */
public class DiffAttributeDelta implements Serializable {

    private static final long serialVersionUID = -4691920884688026746L;
    private String attributeName;
    private Object targetValue;
    private Object referenceValue;

    /**
     * Gets the name of the attribute.
     *
     * @return the attributeName
     */
    public String getAttributeName() {
        return this.attributeName;
    }

    /**
     * Sets the attribute name.
     *
     * @param attributeName
     *            the attribute name to set
     */
    public void setAttributeName(final String attributeName) {
        this.attributeName = attributeName;
    }

    /**
     * Gets the value of the attribute in the target bucket.
     * <p>
     * Will return {@code null} in the following scenarios:
     * <ul>
     * <li>the persistence object that this attribute comes from does not exist the target bucket
     * <li>the actual value of the attribute on the relevant persistence object in the target bucket is indeed {@code null}.</li>
     * </ul>
     *
     * @return the value of the attribute in the target bucket
     */
    public Object getTargetValue() {
        return this.targetValue;
    }

    /**
     * Sets the value of the attribute in the target bucket.
     *
     * @param targetValue
     *            the target value to set
     */
    public void setTargetValue(final Object targetValue) {
        this.targetValue = targetValue;
    }

    /**
     * Gets the value of the attribute in the reference bucket.
     * <p>
     * Will return {@code null} in the following scenarios:
     * <ul>
     * <li>the persistence object that this attribute comes from does not exist in the reference bucket
     * <li>the actual value of the attribute on the relevant persistence object in the reference bucket is indeed {@code null}.</li>
     * </ul>
     *
     * @return the value of the attribute in the reference bucket
     */
    public Object getReferenceValue() {
        return this.referenceValue;
    }

    /**
     * Sets the value of the attribute in the reference bucket.
     *
     * @param referenceValue
     *            the reference value to set
     */
    public void setReferenceValue(final Object referenceValue) {
        this.referenceValue = referenceValue;
    }

}
