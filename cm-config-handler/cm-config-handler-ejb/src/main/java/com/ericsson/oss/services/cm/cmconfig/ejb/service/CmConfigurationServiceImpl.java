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
package com.ericsson.oss.services.cm.cmconfig.ejb.service;

import static com.ericsson.oss.services.cm.log.ErrorHandler.UNEXPECTED_ERROR;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import com.ericsson.oss.itpf.sdk.core.annotation.EServiceQualifier;
import com.ericsson.oss.services.cm.cmconfig.command.CmConfigCommandFactory;
import com.ericsson.oss.services.cm.cmconfig.command.CmConfigCommandObject;
import com.ericsson.oss.services.cm.log.ErrorHandler;
import com.ericsson.oss.services.scriptengine.spi.CommandHandler;
import com.ericsson.oss.services.scriptengine.spi.dtos.Command;
import com.ericsson.oss.services.scriptengine.spi.dtos.CommandResponseDto;

/**
 * {@link CommandHandler} implementation for CM configuration commands.
 * 
 * It uses the {@link EServiceQualifier} equal to config.
 * 
 * 
 */
@Stateless
@EServiceQualifier("config")
@TransactionManagement(TransactionManagementType.CONTAINER)
@SuppressWarnings("PMD.AvoidCatchingThrowable")
public class CmConfigurationServiceImpl implements CommandHandler {

    @Inject
    CmConfigCommandFactory commandFactory;

    @Inject
    ErrorHandler errorHandler;

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public CommandResponseDto execute(final Command command) {
        CommandResponseDto editorResponseDTO = null;
        try {
            final CmConfigCommandObject commandObject = this.commandFactory.createCommandModel(command.getCommand());
            editorResponseDTO = commandObject.execute();
        } catch (final Throwable t) {
            editorResponseDTO = new CommandResponseDto();
            handleError(editorResponseDTO, t);
        }
        editorResponseDTO.setCommand(command.toString());
        return editorResponseDTO;

    }

    private void handleError(final CommandResponseDto editorResponseDTO, final Throwable t) {
        final String errorMessage = this.errorHandler.createErrorMessageFromException(t);
        editorResponseDTO.setStatusCode(UNEXPECTED_ERROR);
        editorResponseDTO.setStatusMessage(errorMessage);
    }

}
