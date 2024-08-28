package com.ericsson.oss.services.cm.cmparser.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

import com.ericsson.oss.services.cm.cmparser.impl.DequeStack;
import com.ericsson.oss.services.cm.cmparser.impl.Stack;

public class DequeStackTest {

    @Test
    public void testPushPopPeekIsEmpty() throws Exception {

        final Stack<String> dequeStack = new DequeStack<>();

        final String objectToPush1 = "something1";
        final String objectToPush2 = "something2";

        dequeStack.push(objectToPush1);
        dequeStack.push(objectToPush2);

        assertThat("stack should not be empty", !dequeStack.isEmpty());
        assertThat(dequeStack.peek(), equalTo(objectToPush2));
        assertThat(dequeStack.pop(), equalTo(objectToPush2));
        assertThat(dequeStack.peek(), equalTo(objectToPush1));
        assertThat(dequeStack.pop(), equalTo(objectToPush1));
        assertThat("stack should be empty", dequeStack.isEmpty());

    }

}
