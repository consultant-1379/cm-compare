package com.ericsson.oss.services.cm.cmconfig.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ericsson.oss.services.scriptengine.spi.dtos.Command;
import com.ericsson.oss.services.scriptengine.spi.dtos.CommandResponseDto;
import com.ericsson.oss.services.scriptengine.spi.dtos.ListDto;

@RunWith(Arquillian.class)
@Stateless
public class CmConfigIT extends CmConfigTestBase {

    private static final String CREATE_CONFIG = "create ";
    private static final String DELETE_CONFIG = "delete ";
    private static final String CONFIG_NAME = "myconfig";
    final Map<String, Object> noFormData = new HashMap<>();

    /*
     * this is the intermediate bean needed as @EServiceRef can not be used
     * directly inside arquillian test see
     * http://eselivm2v238l.lmera.ericsson.se
     * :8081/nexus/content/sites/tor/ServiceFrameworkAPI
     * /1.0.33/sdk-core-api/faq.html#question6
     */

    @Test
    public void canUseCMConfigToExecuteCreateConfigCommand() throws Exception {
        final Command command = new Command("config", CREATE_CONFIG + CONFIG_NAME, noFormData);
        final CommandResponseDto commandResponseDto = cmConfigService.execute(command);
        assertNotNull(command.getCommand());
        assertResponseCodeSuccess(commandResponseDto);
        assertEquals(ListDto.class, commandResponseDto.getCommandResultDto().getClass());

        final Command removeCommand = new Command("config", DELETE_CONFIG + CONFIG_NAME, noFormData);
        final CommandResponseDto responseDto = cmConfigService.execute(removeCommand);
        assertResponseCodeSuccess(responseDto);

    }

    private void assertResponseCodeSuccess(final CommandResponseDto commandResponseDto) {
        assertTrue("Expected SUCCESS response code but got" + commandResponseDto.getStatusCode() + " " + commandResponseDto.getStatusMessage(),
                commandResponseDto.getStatusCode() >= CommandResponseDto.SUCCESS);
    }

    private void assertResultContains(final String result, final String fdn, final String... attributes) {
        assertTrue(result, result.contains(fdn));
        for (final String attribute : attributes) {
            assertTrue(result, result.contains(attribute));
        }
    }

    private void assertResultDoesNotContain(final String result, final String... fdns) {
        for (final String fdn : fdns) {
            assertFalse(result, result.contains(fdn));
        }
    }

    private void assertStringInStatusMessageResponse(final CommandResponseDto commandResponseDto, final String expected) {
        assertTrue(commandResponseDto.getStatusMessage(), commandResponseDto.getStatusMessage().contains(expected));
    }

}
