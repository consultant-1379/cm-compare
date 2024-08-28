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

import javax.enterprise.inject.Default;
import javax.inject.Inject;

import com.ericsson.oss.services.cm.cmconfig.command.handlers.CommandHandler;
import com.ericsson.oss.services.cm.cmconfig.command.handlers.qualifiers.CreateConfig;
import com.ericsson.oss.services.cm.cmconfig.command.handlers.qualifiers.DeleteConfig;
import com.ericsson.oss.services.cm.cmconfig.command.handlers.qualifiers.DiffConfig;
import com.ericsson.oss.services.cm.cmconfig.command.handlers.qualifiers.InvalidCommand;
import com.ericsson.oss.services.cm.cmconfig.command.handlers.qualifiers.LinkConfig;
import com.ericsson.oss.services.cm.cmconfig.command.handlers.qualifiers.ListConfigs;
import com.ericsson.oss.services.cm.cmconfig.command.handlers.qualifiers.LockConfig;
import com.ericsson.oss.services.cm.cmconfig.command.handlers.qualifiers.SetScopeConfig;
import com.ericsson.oss.services.cm.cmconfig.command.handlers.qualifiers.UnlinkConfig;
import com.ericsson.oss.services.cm.cmconfig.command.handlers.qualifiers.UnlockConfig;
import com.ericsson.oss.services.cm.cmconfig.command.handlers.qualifiers.UpdateConfig;
import com.ericsson.oss.services.cm.cmparser.api.CMParserService;
import com.ericsson.oss.services.cm.cmparser.dto.ParserResult;

/**
 * 
 * {@link CmConfigCommandFactory} implementation for creating a
 * {@link CmConfigCommandObject}.
 * 
 */
@Default
public class CmConfigCommandFactoryImpl implements CmConfigCommandFactory {
    @Inject
    CMParserService cmParserService;

    @Inject
    @CreateConfig
    CommandHandler createConfigCommandHandler;

    @Inject
    @ListConfigs
    CommandHandler listConfigsCommandHandler;

    @Inject
    @SetScopeConfig
    CommandHandler setScopeConfigCommandHandler;

    @Inject
    @UpdateConfig
    CommandHandler updateConfigCommandHandler;

    @Inject
    @LockConfig
    CommandHandler lockConfigCommandHandler;

    @Inject
    @UnlockConfig
    CommandHandler unlockConfigCommandHandler;

    @Inject
    @DiffConfig
    CommandHandler diffConfigCommandHandler;

    @Inject
    @DeleteConfig
    CommandHandler deleteConfigCommandHandler;

    @Inject
    @LinkConfig
    CommandHandler linkConfigCommandHandler;

    @Inject
    @UnlinkConfig
    CommandHandler unlinkConfigCommandHandler;

    @Inject
    @InvalidCommand
    CommandHandler invalidCommandHandler;

    @Override
    public CmConfigCommandObject createCommandModel(final String commandString) {
        final CmConfigCommandObject cmCommandObject = new CmConfigCommandObject();
        final ParserResult command = this.cmParserService.parseCommand(commandString);
        cmCommandObject.setParserResult(command);
        switch (command.getCommandType()) {
        case CREATE_CONFIG_COMMAND:
            cmCommandObject.setCommandHandler(this.createConfigCommandHandler);
            break;
        case LIST_CONFIG_COMMAND:
            cmCommandObject.setCommandHandler(this.listConfigsCommandHandler);
            break;
        case SET_SCOPE_CONFIG_COMMAND:
            cmCommandObject.setCommandHandler(this.setScopeConfigCommandHandler);
            break;
        case UPDATE_CONFIG_COMMAND:
            cmCommandObject.setCommandHandler(this.updateConfigCommandHandler);
            break;
        case LOCK_CONFIG_COMMAND:
            cmCommandObject.setCommandHandler(this.lockConfigCommandHandler);
            break;
        case UNLOCK_CONFIG_COMMAND:
            cmCommandObject.setCommandHandler(this.unlockConfigCommandHandler);
            break;
        case DIFF_CONFIG_COMMAND:
            cmCommandObject.setCommandHandler(this.diffConfigCommandHandler);
            break;
        case DELETE_CONFIG_COMMAND:
            cmCommandObject.setCommandHandler(this.deleteConfigCommandHandler);
            break;
        case LINK_CONFIG_COMMAND:
            cmCommandObject.setCommandHandler(this.linkConfigCommandHandler);
            break;
        case UNLINK_CONFIG_COMMAND:
            cmCommandObject.setCommandHandler(this.unlinkConfigCommandHandler);
            break;
        default:
            // Includes parserError
            cmCommandObject.setCommandHandler(this.invalidCommandHandler);
        }
        return cmCommandObject;
    }

}
