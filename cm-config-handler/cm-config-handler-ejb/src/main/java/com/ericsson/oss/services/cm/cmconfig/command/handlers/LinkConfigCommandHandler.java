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

import com.ericsson.oss.itpf.sdk.core.annotation.EServiceRef;
import com.ericsson.oss.services.cm.cmcompare.api.CmCompareService;
import com.ericsson.oss.services.cm.cmconfig.command.handlers.qualifiers.LinkConfig;
import com.ericsson.oss.services.cm.cmparser.dto.ParserResult;
import com.ericsson.oss.services.cm.cmshared.dto.CmResponse;

/**
 * 
 * Link Config command handler.
 * 
 */
@LinkConfig
public class LinkConfigCommandHandler extends CommandHandler {

    @EServiceRef
    CmCompareService cmCompareService;

    @Override
    public CmResponse getCmResponse(final ParserResult parserResult) {

        return this.cmCompareService.linkConfig(parserResult.getConfigurationName(), parserResult.getBaseConfigurationName());

    }
}
