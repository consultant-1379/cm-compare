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
package com.ericsson.oss.services.cm.config.service.test;

import com.ericsson.oss.itpf.sdk.core.annotation.EServiceRef;
import com.ericsson.oss.services.cm.cmconfig.service.api.CmConfigService;

/**
 * Proxy (wrapper) required for resolving EService Reference of {@link CmConfigService}.
 */
public class CmConfigServiceProxyBean implements CmConfigServiceProxy {

    @EServiceRef
    protected CmConfigService cmConfigService;

    @Override
    public CmConfigService getCmConfigService() {
        return this.cmConfigService;
    }

}
