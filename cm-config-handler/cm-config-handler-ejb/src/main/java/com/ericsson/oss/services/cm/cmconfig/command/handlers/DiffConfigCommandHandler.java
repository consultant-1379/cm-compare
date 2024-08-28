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
import com.ericsson.oss.services.cm.cmcompare.api.CmConfigDiffParams;
import com.ericsson.oss.services.cm.cmcompare.api.CmConfigDiffParams.CmConfigDiff;
import com.ericsson.oss.services.cm.cmconfig.command.handlers.qualifiers.DiffConfig;
import com.ericsson.oss.services.cm.cmparser.dto.ParserResult;
import com.ericsson.oss.services.cm.cmshared.dto.CmResponse;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchCriteria;

/**
 * 
 * Diff Config command handler.
 * 
 */
@DiffConfig
public class DiffConfigCommandHandler extends CommandHandler {

    @EServiceRef
    CmCompareService cmCompareService;

    @Override
    public CmResponse getCmResponse(final ParserResult parserResult) {

        final CmSearchCriteria cmSearchCriteria = getCmSearchCriteria(parserResult);
        final CmConfigDiffParams cmConfigDiffParams = getCmConfigDiffParams(parserResult);

        return this.cmCompareService.diffConfig(parserResult.getConfigurationName(), parserResult.getSourceConfigurationName(), cmSearchCriteria,
                cmConfigDiffParams);

    }

    private CmConfigDiffParams getCmConfigDiffParams(final ParserResult parserResult) {

        final CmConfigDiffParams cmConfigDiffParams = new CmConfigDiffParams();

        if (parserResult.getDiffConfigOptions().isDiffConfigDeepOption()) {
            cmConfigDiffParams.addCmDiffParam(CmConfigDiff.deep);
        }

        if (parserResult.getDiffConfigOptions().isDiffConfigUpdateOption()) {
            cmConfigDiffParams.addCmDiffParam(CmConfigDiff.update);
            cmConfigDiffParams.setUpdateConfigName(parserResult.getDiffConfigOptions().getDiffConfigUpdateName());
        }
        if (parserResult.getDiffConfigOptions().isDiffConfigImportOption()) {
            cmConfigDiffParams.addCmDiffParam(CmConfigDiff.output);
        }

        return cmConfigDiffParams;

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
