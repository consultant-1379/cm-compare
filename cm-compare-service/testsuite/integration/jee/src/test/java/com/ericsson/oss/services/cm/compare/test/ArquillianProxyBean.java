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

import java.util.Map;

import javax.ejb.Stateless;

import com.ericsson.oss.itpf.sdk.core.annotation.EServiceRef;
import com.ericsson.oss.services.cm.cmcompare.api.CmCompareService;
import com.ericsson.oss.services.cm.cmcompare.api.CmConfigDiffParams;
import com.ericsson.oss.services.cm.cmcompare.api.CmConfigUpdateParams;
import com.ericsson.oss.services.cm.cmshared.dto.CmObjectSpecification;
import com.ericsson.oss.services.cm.cmshared.dto.CmResponse;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchCriteria;

@Stateless
public class ArquillianProxyBean implements ArquillianProxy {

    @EServiceRef
    protected CmCompareService cmCompare;
    
    @Override
	public CmResponse createConfig(final String configName) {
		return cmCompare.createConfig(configName);
	}

	@Override
	public CmResponse listConfig() {
 		return cmCompare.listConfig();
	}

	@Override
	public CmResponse createConfig(String configName, CmSearchCriteria scope) {
		return cmCompare.createConfig(configName,scope);
	}

	@Override
	public CmResponse updateConfig(String toConfigName, String fromConfigName,
			CmSearchCriteria scope, CmConfigUpdateParams params) {
		return cmCompare.updateConfig(toConfigName, fromConfigName, scope, params);
	}

	@Override
	public CmResponse diffConfig(String toConfigName, String fromConfigName,
			CmSearchCriteria scope, CmConfigDiffParams params) {
		return cmCompare.diffConfig(toConfigName, fromConfigName, scope, params);
	}

	@Override
	public CmResponse linkConfig(String overlayConfigName, String baseConfigName) {
		return cmCompare.linkConfig(overlayConfigName, baseConfigName);
	}

	@Override
	public CmResponse unlinkConfig(String overlayConfigName) {
		return cmCompare.unlinkConfig(overlayConfigName);
	}

	@Override
	public CmResponse lockConfig(String configName, String reasonString) {
		return cmCompare.lockConfig(configName, reasonString);
	}

	@Override
	public CmResponse unlockConfig(String configName) {
		return cmCompare.unlockConfig(configName);
	}

	@Override
	public CmResponse deleteConfig(String configName) {
		return cmCompare.deleteConfig(configName);
	}
	
}
