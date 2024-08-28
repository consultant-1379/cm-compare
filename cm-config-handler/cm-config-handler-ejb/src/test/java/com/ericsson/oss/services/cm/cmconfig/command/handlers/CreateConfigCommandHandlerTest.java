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
package com.ericsson.oss.services.cm.cmconfig.command.handlers;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ericsson.oss.services.cm.cmcompare.api.CmCompareService;
import com.ericsson.oss.services.cm.cmconfig.command.handlers.CreateConfigCommandHandler;
import com.ericsson.oss.services.cm.cmparser.dto.ParserResult;
import com.ericsson.oss.services.cm.cmshared.dto.CmResponse;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchScope;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchScope.ScopeType;
import com.ericsson.oss.services.scriptengine.spi.dtos.CommandResponseDto;

@RunWith(MockitoJUnitRunner.class)
public class CreateConfigCommandHandlerTest {

    @Mock
    CmCompareService cmCompareService;

    @Mock
    ParserResult parserResult;

    @Mock
    CmResponse cmResponse;

    CmSearchScope cmSearchScope = new CmSearchScope();

    @InjectMocks
    // this will do all the magic!! Mockito will inject your mocked session
    // façade into the appropriate field … so no need for a @Before method
    CreateConfigCommandHandler objUnderTest;

    @Test
    public void processCreateConfigCommandWithoutSetScope() {

        when(cmCompareService.createConfig(anyString())).thenReturn(cmResponse);
        // execute the test
        final CommandResponseDto result = objUnderTest.processCommand(parserResult);

        // Check that the expect service call was made
        verify(cmCompareService).createConfig(anyString());
    }

    @Test
    public void processCreateConfigCommandForFdnScope() {
        final String fdn = "fdn";
        mockForGivenScope(ScopeType.FDN, fdn);
        when(cmCompareService.createConfig(anyString())).thenReturn(cmResponse);
        // execute the test
        final CommandResponseDto result = objUnderTest.processCommand(parserResult);

        // Check that the expect service call was made
        verify(cmCompareService).createConfig(anyString());
    }

    private void mockForGivenScope(final ScopeType scopeType, final String scopeValue) {
        final List<CmSearchScope> cmSearchScopes = new ArrayList<>();
        cmSearchScope.setScopeType(scopeType);
        cmSearchScope.setValue(scopeValue);
        cmSearchScopes.add(cmSearchScope);
        when(parserResult.getCmScopes()).thenReturn(cmSearchScopes);
        when(cmResponse.getStatusCode()).thenReturn(-1); // Don't care about the response for this test, set to error so less mocking required
    }

}
