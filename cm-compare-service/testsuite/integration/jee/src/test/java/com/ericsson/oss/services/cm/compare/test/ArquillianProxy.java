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
package com.ericsson.oss.services.cm.compare.test;

import javax.ejb.Remote;

import com.ericsson.oss.itpf.sdk.core.annotation.EService;
import com.ericsson.oss.services.cm.cmcompare.api.CmCompareService;


@Remote
@EService
public interface ArquillianProxy extends CmCompareService {

    // Just an compound intermediate interface for testing purposes 

}
