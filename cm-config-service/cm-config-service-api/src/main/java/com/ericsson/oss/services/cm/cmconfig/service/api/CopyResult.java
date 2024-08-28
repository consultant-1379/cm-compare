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
 * Contains the result of a single node copy.
 */
public class CopyResult implements Serializable {
    private static final long serialVersionUID = -1525886122630192198L;

    /**
     * Status of the copy.
     */
    public enum CopyStatus {
        /** Node was copied successfully. */
        COPIED,

        /** Node was not copied, the error message should contain more information. */
        NOT_COPIED,

        /** Node was copied, but it already existed in target, so was overwritten. */
        OVERWRITEN,

        /** A search criteria was supplied, and no matches were found. */
        NO_MATCHES_FOUND,
    }

    private final String nodeName;
    private final String fdn;
    private CopyStatus copyStatus;
    private String statusMessage;

    /**
     * Constructor with node name and FDN.
     *
     * @param nodeName
     *            the name of the node
     * @param fdn
     *            the FDN of the node
     */
    public CopyResult(final String nodeName, final String fdn) {
        this(nodeName, fdn, null, "");
    }

    /**
     * Constructor with node name, FDN, copy status and error message.
     *
     * @param nodeName
     *            the name of the node
     * @param fdn
     *            the FDN of the node
     * @param copyStatus
     *            the status of the copy
     * @param statusMessage
     *            the status message, should be "" if none is needed
     */
    public CopyResult(final String nodeName, final String fdn, final CopyStatus copyStatus, final String statusMessage) {
        this.nodeName = nodeName;
        this.fdn = fdn;
        this.copyStatus = copyStatus;
        this.statusMessage = statusMessage;
    }

    /**
     * Gets the status of the copy.
     *
     * @return the status
     */
    public CopyStatus getCopyStatus() {
        return this.copyStatus;
    }

    /**
     * Gets the status message.
     *
     * @return the status message
     */
    public String getStatusMessage() {
        return this.statusMessage;
    }

    /**
     * Gets the name of the node.
     *
     * @return the name of the node.
     */
    public String getNodeName() {
        return this.nodeName;
    }

    /**
     * Gets the FDN of the node.
     *
     * @return the FDN of the node
     */
    public String getFdn() {
        return this.fdn;
    }

    /**
     * Sets the copy status.
     *
     * @param copyStatus
     *            the copy status
     */
    public void setCopyStatus(final CopyStatus copyStatus) {
        this.copyStatus = copyStatus;
    }

    /**
     * Sets the status message.
     *
     * @param statusMessage
     *            the error message
     */
    public void setStatusMessage(final String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
