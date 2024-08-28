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
import com.ericsson.oss.services.cm.cmconfig.command.handlers.qualifiers.CreateConfig;
import com.ericsson.oss.services.cm.cmparser.dto.ParserResult;
import com.ericsson.oss.services.cm.cmshared.dto.CmResponse;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchCriteria;

/**
 * 
 * Create Config command handler.
 * 
 */
@CreateConfig
public class CreateConfigCommandHandler extends CommandHandler {

    @EServiceRef
    CmCompareService cmCompareService;

    @Override
    public CmResponse getCmResponse(final ParserResult parserResult) {

        final CmSearchCriteria cmSearchCriteria = new CmSearchCriteria();

        cmSearchCriteria.setCmSearchScopes(parserResult.getCmScopes());

        if (parserResult.getCmObjectSpecifications().isEmpty()) {
            cmSearchCriteria.setSingleCmObjectSpecification(createDefaultCmObjectSpecifcation());
        } else {
            cmSearchCriteria.setCmObjectSpecifications(parserResult.getCmObjectSpecifications());
        }

        return this.cmCompareService.createConfig(parserResult.getConfigurationName());

    }

}
