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

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 
 * This class represents a stack used during the parsing process.
 * 
 * @param <T>
 */
public class DequeStack<T> implements Stack<T> {

    private final Deque<T> deque = new ArrayDeque<>();

    @Override
    public void push(final T object) {
        this.deque.addFirst(object);
    }

    @Override
    public T pop() {
        return this.deque.removeFirst();
    }

    @Override
    public T peek() {
        return this.deque.peekFirst();
    }

    @Override
    public boolean isEmpty() {
        return this.deque.isEmpty();
    }

    @Override
    public int size() {
        return this.deque.size();
    }
}
