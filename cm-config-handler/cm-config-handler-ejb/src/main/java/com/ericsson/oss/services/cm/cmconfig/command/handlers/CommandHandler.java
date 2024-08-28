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

import com.ericsson.oss.services.cm.cmconfig.utilities.ResponseFormatter;
import com.ericsson.oss.services.cm.cmparser.dto.ParserResult;
import com.ericsson.oss.services.cm.cmshared.dto.CmObjectSpecification;
import com.ericsson.oss.services.cm.cmshared.dto.CmResponse;
import com.ericsson.oss.services.scriptengine.spi.dtos.CommandResponseDto;

/**
 * 
 * Generic Command Handler implementation.
 * 
 */
public abstract class CommandHandler {

    static final String DEFAULT_SPECIFICATION_TYPE = "MeContext";
    static final String DEFAULT_SPECIFICATION_NAMESPACE = "OSS_TOP";

    // TODO Inject formatter using @Inject
    final ResponseFormatter responseFormatter = new ResponseFormatter();

    /**
     * Get the {@link CmResponse} for a corresponding {@link ParserResult}
     * param.
     * 
     * @param command
     *            {@link ParserResult} object
     * @return CmResponse
     */
    abstract CmResponse getCmResponse(ParserResult command);

    /**
     * Process the cofig command represented by the {@link ParserResult} object.
     * 
     * @param command
     *            {@link ParserResult} object
     * @return CommandResponseDto
     */
    public CommandResponseDto processCommand(final ParserResult command) {
        final CmResponse cmResponse = this.getCmResponse(command);
        if (command.outputTable()) {
            return this.responseFormatter.generateCommandResponseDtoTableFormat(cmResponse);
        }
        return this.responseFormatter.generateCommandResponseDtoListFormat(cmResponse);
    }

    /**
     * Create a default {@link CmObjectSpecification} (Type Specification).
     * 
     * @return CmObjectSpecification
     */
    protected CmObjectSpecification createDefaultCmObjectSpecifcation() {
        final CmObjectSpecification defaultCmObjectSpeecification;
        defaultCmObjectSpeecification = new CmObjectSpecification();
        defaultCmObjectSpeecification.setAttributeSpecificationContainer(CmObjectSpecification.ALL_ATTRIBUTES);
        defaultCmObjectSpeecification.setNamespace(DEFAULT_SPECIFICATION_NAMESPACE);
        defaultCmObjectSpeecification.setType(DEFAULT_SPECIFICATION_TYPE);
        return defaultCmObjectSpeecification;
    }

}
