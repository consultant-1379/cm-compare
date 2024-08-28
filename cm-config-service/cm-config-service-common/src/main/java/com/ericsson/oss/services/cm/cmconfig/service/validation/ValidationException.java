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
package com.ericsson.oss.services.cm.cmconfig.service.validation;

/**
 * Validation Exception implementation.
 */
public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = 5496664793544977316L;
    private final String suggestedSolution;

    /**
     * Constructor which takes the message of the exception.
     *
     * @param message
     *            of the exception
     * @param suggestedSolution
     *            suggested solution
     */
    public ValidationException(final String message, final String suggestedSolution) {
        super(message);
        this.suggestedSolution = suggestedSolution;
    }

    /**
     * Suggested solution.
     *
     * @return the suggestedSolution
     */
    public String getSuggestedSolution() {
        return this.suggestedSolution;
    }

}
