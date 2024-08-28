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
import com.ericsson.oss.services.cm.cmcompare.api.CmConfigUpdateParams;
import com.ericsson.oss.services.cm.cmcompare.api.CmConfigUpdateParams.CmConfigUpdate;
import com.ericsson.oss.services.cm.cmconfig.command.handlers.qualifiers.UpdateConfig;
import com.ericsson.oss.services.cm.cmparser.dto.ParserResult;
import com.ericsson.oss.services.cm.cmshared.dto.CmResponse;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchCriteria;

/**
 * 
 * Update Config command handler.
 * 
 */
@UpdateConfig
public class UpdateConfigCommandHandler extends CommandHandler {

    @EServiceRef
    CmCompareService cmCompareService;

    @Override
    public CmResponse getCmResponse(final ParserResult parserResult) {

        final CmSearchCriteria cmSearchCriteria = getCmSearchCriteria(parserResult);
        final CmConfigUpdateParams cmConfigUpdateParams = getCmConfigUpdateParams(parserResult);

        return this.cmCompareService.updateConfig(parserResult.getConfigurationName(), parserResult.getSourceConfigurationName(), cmSearchCriteria,
                cmConfigUpdateParams);

    }

    private CmConfigUpdateParams getCmConfigUpdateParams(final ParserResult parserResult) {

        final CmConfigUpdateParams cmConfigUpdateParams = new CmConfigUpdateParams();

        if (parserResult.getUpdateConfigOptions().isUpdateConfigCreateOption()) {
            cmConfigUpdateParams.addCmUpdateParam(CmConfigUpdate.create);
        }

        if (parserResult.getUpdateConfigOptions().isUpdateConfigDeleteOption()) {
            cmConfigUpdateParams.addCmUpdateParam(CmConfigUpdate.delete);
        }
        if (parserResult.getUpdateConfigOptions().isUpdateConfigOverwriteOption()) {
            cmConfigUpdateParams.addCmUpdateParam(CmConfigUpdate.overwrite);
        }
        return cmConfigUpdateParams;

    }

    private CmSearchCriteria getCmSearchCriteria(final ParserResult parserResult) {

        final CmSearchCriteria cmSearchCriteria = new CmSearchCriteria();
        cmSearchCriteria.setCmSearchScopes(parserResult.getCmScopes());

        if (parserResult.getCmObjectSpecifications().isEmpty()) {
            cmSearchCriteria.setSingleCmObjectSpecification(createDefaultCmObjectSpecifcation());
        } else {
            cmSearchCriteria.setCmObjectSpecifications(parserResult.getCmObjectSpecifications());
        }
        return cmSearchCriteria;
    }
}
