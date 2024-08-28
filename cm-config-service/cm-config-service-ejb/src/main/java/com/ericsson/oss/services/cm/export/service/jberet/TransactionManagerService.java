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
package com.ericsson.oss.services.cm.export.service.jberet;

import javax.naming.InitialContext;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

public class TransactionManagerService {

    public TransactionManager getTransactionManagerFromJNDI() {
        try {
            final InitialContext ctx = new InitialContext();
            return (TransactionManager) ctx.lookup("java:/TransactionManager");
        } catch (final Exception e) {
        }
        return null;
    }

    public TransactionManager getTransactionManagerFromUserTransaction() throws Exception {
        final InitialContext ctx = new InitialContext();
        final UserTransaction ut = (UserTransaction) ctx.lookup("java:comp/UserTransaction");
        if (ut instanceof TransactionManager) {
            //UserTransaction is also TransactionManager
            return (TransactionManager) ut;
        }
        return null;
    }

}
