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
package com.ericsson.oss.services.cm.cmparser.impl;

/**
 * 
 * This class represents a stack used during the parsing process.
 * 
 * @param <T>
 */
public interface Stack<T> {
    /**
     * Push one element into the stack.
     * 
     * @param object
     *            to add to the stack
     */
    void push(T object);

    /**
     * Pop one element from the stack.
     * 
     * @return one element from the stack
     */
    T pop();

    /**
     * Peek one element from the stack.
     * 
     * @return one element from the stack
     */
    T peek();

    /**
     * Check if the stack is empty.
     * 
     * @return true is the stack is empty
     */
    boolean isEmpty();

    /**
     * Obtain the size of the stack.
     * 
     * @return the number of elements in the stack
     */
    int size();
}
