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
package com.ericsson.oss.services.cm.cmconfig.command;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ericsson.oss.services.cm.cmconfig.command.CmConfigCommandFactoryImpl;
import com.ericsson.oss.services.cm.cmconfig.command.CmConfigCommandObject;
import com.ericsson.oss.services.cm.cmconfig.command.handlers.CommandHandler;
import com.ericsson.oss.services.cm.cmconfig.command.handlers.qualifiers.CreateConfig;
import com.ericsson.oss.services.cm.cmconfig.command.handlers.qualifiers.DeleteConfig;
import com.ericsson.oss.services.cm.cmconfig.command.handlers.qualifiers.DiffConfig;
import com.ericsson.oss.services.cm.cmconfig.command.handlers.qualifiers.InvalidCommand;
import com.ericsson.oss.services.cm.cmconfig.command.handlers.qualifiers.ListConfigs;
import com.ericsson.oss.services.cm.cmconfig.command.handlers.qualifiers.LockConfig;
import com.ericsson.oss.services.cm.cmconfig.command.handlers.qualifiers.SetScopeConfig;
import com.ericsson.oss.services.cm.cmconfig.command.handlers.qualifiers.UnlockConfig;
import com.ericsson.oss.services.cm.cmconfig.command.handlers.qualifiers.UpdateConfig;
import com.ericsson.oss.services.cm.cmparser.api.CMParserService;
import com.ericsson.oss.services.cm.cmparser.dto.ParserResult;
import com.ericsson.oss.services.cm.cmparser.dto.ParserResult.CommandType;

@RunWith(MockitoJUnitRunner.class)
public class CmConfigCommandFactoryTest {

    @Mock
    @Inject
    CMParserService mockedCmParserService;

    @Mock
    @CreateConfig
    CommandHandler createConfigCommandHandler;

    @Mock
    @ListConfigs
    CommandHandler listConfigsCommandHandler;

    @Mock
    @SetScopeConfig
    CommandHandler setScopeConfigCommandHandler;

    @Mock
    @UpdateConfig
    CommandHandler updateConfigCommandHandler;

    @Mock
    @LockConfig
    CommandHandler lockConfigCommandHandler;

    @Mock
    @UnlockConfig
    CommandHandler unlockConfigommandHandler;

    @Mock
    @DiffConfig
    CommandHandler diffConfigCommandHandler;

    @Mock
    @DeleteConfig
    CommandHandler deleteConfigCommandHandler;

    @Mock
    @DeleteConfig
    CommandHandler linkConfigCommandHandler;

    @Mock
    @DeleteConfig
    CommandHandler unlinkConfigCommandHandler;

    @Mock
    @InvalidCommand
    CommandHandler invalidCommandHandler;

    @InjectMocks
    // this will do all the magic!! Mockito will inject your mocked session
    // façade into the appropriate field … so no need for a @Before method
    CmConfigCommandFactoryImpl objUnderTest;

    @Test
    public void createCommandModelReturnsCmCommandObjectWithCreateConfigCommandHandler() {
        final ParserResult getCommand = new ParserResult();
        getCommand.setCommandType(CommandType.CREATE_CONFIG_COMMAND);
        createCommandModelReturnsACmCommandObjectWithTheCorrectCommandAndHandler(getCommand, createConfigCommandHandler);
    }

    @Test
    public void createCommandModelReturnsCmCommandObjectWithListConfigCommandHandler() {
        final ParserResult createCommand = new ParserResult();
        createCommand.setCommandType(CommandType.LIST_CONFIG_COMMAND);
        createCommandModelReturnsACmCommandObjectWithTheCorrectCommandAndHandler(createCommand, listConfigsCommandHandler);
    }

    @Test
    public void createCommandModelReturnsCmCommandObjectWithSetScopeConfigCommandHandler() {
        final ParserResult createCommand = new ParserResult();
        createCommand.setCommandType(CommandType.SET_SCOPE_CONFIG_COMMAND);
        createCommandModelReturnsACmCommandObjectWithTheCorrectCommandAndHandler(createCommand, setScopeConfigCommandHandler);
    }

    @Test
    public void createCommandModelReturnsCmCommandObjectWithLockConfigCommandHandler() {
        final ParserResult createCommand = new ParserResult();
        createCommand.setCommandType(CommandType.LOCK_CONFIG_COMMAND);
        createCommandModelReturnsACmCommandObjectWithTheCorrectCommandAndHandler(createCommand, lockConfigCommandHandler);
    }

    @Test
    public void createCommandModelReturnsCmCommandObjectWithUnlockConfigCommandHandler() {
        final ParserResult createCommand = new ParserResult();
        createCommand.setCommandType(CommandType.UNLOCK_CONFIG_COMMAND);
        createCommandModelReturnsACmCommandObjectWithTheCorrectCommandAndHandler(createCommand, unlockConfigommandHandler);
    }

    @Test
    public void createCommandModelReturnsCmCommandObjectWithUpdateConfigCommandHandler() {
        final ParserResult createCommand = new ParserResult();
        createCommand.setCommandType(CommandType.UPDATE_CONFIG_COMMAND);
        createCommandModelReturnsACmCommandObjectWithTheCorrectCommandAndHandler(createCommand, updateConfigCommandHandler);
    }

    @Test
    public void createCommandModelReturnsCmCommandObjectWithDiffConfigCommandHandler() {
        final ParserResult createCommand = new ParserResult();
        createCommand.setCommandType(CommandType.DIFF_CONFIG_COMMAND);
        createCommandModelReturnsACmCommandObjectWithTheCorrectCommandAndHandler(createCommand, diffConfigCommandHandler);
    }

    @Test
    public void createCommandModelReturnsCmCommandObjectWithDeleteConfigCommandHandler() {
        final ParserResult createCommand = new ParserResult();
        createCommand.setCommandType(CommandType.DELETE_CONFIG_COMMAND);
        createCommandModelReturnsACmCommandObjectWithTheCorrectCommandAndHandler(createCommand, deleteConfigCommandHandler);
    }

    @Test
    public void createCommandModelReturnsCmCommandObjectWithLinkConfigCommandHandler() {
        final ParserResult createCommand = new ParserResult();
        createCommand.setCommandType(CommandType.LINK_CONFIG_COMMAND);
        createCommandModelReturnsACmCommandObjectWithTheCorrectCommandAndHandler(createCommand, linkConfigCommandHandler);
    }

    @Test
    public void createCommandModelReturnsCmCommandObjectWithUnlinkConfigCommandHandler() {
        final ParserResult createCommand = new ParserResult();
        createCommand.setCommandType(CommandType.UNLINK_CONFIG_COMMAND);
        createCommandModelReturnsACmCommandObjectWithTheCorrectCommandAndHandler(createCommand, unlinkConfigCommandHandler);
    }

    @Test
    public void createCommandModelReturnsCmCommandObjectWithInvalidCommandHandlerForAPaserError() {
        final ParserResult parserError = new ParserResult();
        parserError.setCommandType(CommandType.PARSER_ERROR);
        createCommandModelReturnsACmCommandObjectWithTheCorrectCommandAndHandler(parserError, invalidCommandHandler);
    }

    private void createCommandModelReturnsACmCommandObjectWithTheCorrectCommandAndHandler(final ParserResult mockedCommand,
            final CommandHandler expectedCommandHandler) {
        final String someCommand = "some parserResult";
        // Set-up the mock to return the desired type of parserResult
        when(mockedCmParserService.parseCommand(someCommand)).thenReturn(mockedCommand);
        // Execute the test
        final CmConfigCommandObject cmCommandObject = objUnderTest.createCommandModel(someCommand);
        // Verify that the result contain the correct (type of) objects
        assertEquals(mockedCommand, cmCommandObject.getParserResult());
        assertEquals(expectedCommandHandler, cmCommandObject.getCommandHandler());
        // final Verify that the final correct method was final called with the
        // final correct parameters
        verify(mockedCmParserService).parseCommand(someCommand);
    }

}
