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
package com.ericsson.oss.services.cm.cmparser.api;

import com.ericsson.oss.services.cm.cmparser.dto.ParserResult;

/**
 * 
 * The CMParserService can parse advanced CLI configuration commands.
 * 
 */
public interface CMParserService {
    /**
     * Parse the CLI config command.
     * 
     * @param source
     *            The String representation of the CLI config command
     * @return ParserResult with the result of the parsing
     */
    ParserResult parseCommand(String source);

}
