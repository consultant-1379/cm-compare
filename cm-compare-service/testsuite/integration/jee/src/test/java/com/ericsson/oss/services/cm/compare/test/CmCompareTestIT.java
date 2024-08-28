package com.ericsson.oss.services.cm.compare.test;

import static org.junit.Assert.assertTrue;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ericsson.oss.services.cm.cmshared.dto.CmResponse;


@RunWith(Arquillian.class)
@Stateless
public class CmCompareTestIT extends CmCompareTestBase {

    @EJB
    ArquillianProxy arquillianProxyBean;

    
    @Test
    public void createConfig_configName_configCreated() {
        logger.debug("@Test: createConfig_configName_configCreated");
        final CmResponse cmResponse = arquillianProxyBean.createConfig("NewConfig");
        assertTrue("status message:" + cmResponse.getStatusMessage(), (cmResponse.getStatusCode() >= 0));
    }
    
    @Ignore
    @Test
    public void createConfig_duplicateConfigName_configNotCreated() {
        logger.debug("@Test: createConfig_duplicateConfigName_configNotCreated");
        arquillianProxyBean.createConfig("NewConfig");
        final CmResponse cmResponse = arquillianProxyBean.createConfig("NewConfig");
        //logger.debug(cmResponse.getStatusMessage());
        assertTrue("unexpected status message:" + cmResponse.getStatusMessage(), (cmResponse.getStatusCode() < 0));
    }
    
        
}
