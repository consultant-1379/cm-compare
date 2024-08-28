package com.ericsson.oss.services.cm.compare.test;

import static org.junit.Assert.assertTrue;

import java.util.*;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ericsson.oss.services.cm.cmshared.dto.CmObject;
import com.ericsson.oss.services.cm.cmshared.dto.CmResponse;


@RunWith(Arquillian.class)
@Stateless
public class CmConfigListIT extends CmCompareTestBase {
	
	private static final String [] configs = {"Config1","Config2","Live"};
	
    @EJB
    ArquillianProxy arquillianProxyBean;

    @Test
    public void listConfig_all_allconfigsListed() {
        logger.debug("@Test: listConfig_all_configsListed");
        arquillianProxyBean.createConfig(configs[0]);
        arquillianProxyBean.createConfig(configs[1]);
        final CmResponse cmResponse = arquillianProxyBean.listConfig();
        
        assertTrue("status message:" + cmResponse.getStatusMessage(), (cmResponse.getStatusCode() >= 0));
        assertTrue("Configs in dps # :" +cmResponse.getCmObjects().size() , (cmResponse.getCmObjects().size() == configs.length));
    }
        
}
