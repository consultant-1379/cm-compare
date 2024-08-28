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
package com.ericsson.oss.services.cm.cmconfig.service.validation;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static com.ericsson.oss.services.cm.cmconfig.service.log.CmConfigServiceMessages.CONFIGURATION_PARAM_DOES_NOT_EXIST_ERROR;
import static com.ericsson.oss.services.cm.cmconfig.service.log.CmConfigServiceMessages.CONFIG_COPY_NODES_NOT_SPECIFIED_MESSAGE;
import static com.ericsson.oss.services.cm.cmconfig.service.log.CmConfigServiceMessages.CONFIG_COPY_NODES_SCOPE_NOT_CORRECT;
import static com.ericsson.oss.services.cm.cmconfig.service.log.CmConfigServiceMessages.CONFIG_PARAM_IS_NOT_NON_LIVE_ERROR;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ericsson.oss.itpf.datalayer.dps.DataBucket;
import com.ericsson.oss.itpf.datalayer.dps.DataPersistenceService;
import com.ericsson.oss.itpf.datalayer.dps.NonLiveDataBucket;
import com.ericsson.oss.itpf.datalayer.dps.exception.general.ObjectNotFoundException;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchScope;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchScope.ScopeType;

/**
 * unit test for {@link ValidationConfigServiceBean}.
 */
@SuppressWarnings("PMD.AvoidThrowingRawExceptionTypes")
@RunWith(MockitoJUnitRunner.class)
public class ValidationConfigServiceBeanTest {

    private static final String CONFIGURATION = "config";

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Mock
    private DataPersistenceService mockDataPersistenceService;

    @Mock
    private DataBucket mockBucket;

    @Mock
    private NonLiveDataBucket mockNonLiveBucket;

    @InjectMocks
    private ValidationConfigServiceBean objUnderTest;

    @Test
    public void validateConfigWithExistingConfig() {
        when(this.mockDataPersistenceService.getDataBucket(CONFIGURATION)).thenReturn(this.mockBucket);
        this.objUnderTest.validateConfig(CONFIGURATION);
        verify(this.mockDataPersistenceService).getDataBucket(CONFIGURATION);
    }

    @Test
    public void validateConfigWithNonExistingConfig() {
        when(this.mockDataPersistenceService.getDataBucket(CONFIGURATION)).thenThrow(ObjectNotFoundException.class);
        this.exception.expect(ValidationException.class);

        final MessageFormat messageFormat = new MessageFormat(CONFIGURATION_PARAM_DOES_NOT_EXIST_ERROR);
        this.exception.expectMessage(messageFormat.format(new Object[] { CONFIGURATION }));

        this.objUnderTest.validateConfig(CONFIGURATION);
        verify(this.mockDataPersistenceService).getDataBucket(CONFIGURATION);
    }

    @Test
    public void validateNonLiveConfigWithExistingLiveConfig() {
        when(this.mockDataPersistenceService.getDataBucket("Live")).thenReturn(this.mockBucket);
        when(this.mockBucket.isLiveBucket()).thenReturn(true);
        this.exception.expect(ValidationException.class);

        final MessageFormat messageFormat = new MessageFormat(CONFIG_PARAM_IS_NOT_NON_LIVE_ERROR);
        this.exception.expectMessage(messageFormat.format(new Object[] { "Live" }));

        this.objUnderTest.validateNonLiveConfig("Live");
        verify(this.mockDataPersistenceService).getDataBucket("Live");
        verify(this.mockBucket).isLiveBucket();
    }

    @Test
    public void validateNonLiveConfigWithExistingNonLiveConfig() {
        when(this.mockDataPersistenceService.getDataBucket(CONFIGURATION)).thenReturn(this.mockNonLiveBucket);
        when(this.mockNonLiveBucket.isLiveBucket()).thenReturn(false);

        this.objUnderTest.validateNonLiveConfig(CONFIGURATION);
        verify(this.mockDataPersistenceService).getDataBucket(CONFIGURATION);
        verify(this.mockNonLiveBucket).isLiveBucket();
    }

    @Test
    public void validateNonLiveConfigWithNonExistingConfig() {
        when(this.mockDataPersistenceService.getDataBucket(CONFIGURATION)).thenThrow(ObjectNotFoundException.class);
        this.exception.expect(ValidationException.class);

        final MessageFormat messageFormat = new MessageFormat(CONFIGURATION_PARAM_DOES_NOT_EXIST_ERROR);
        this.exception.expectMessage(messageFormat.format(new Object[] { CONFIGURATION }));

        this.objUnderTest.validateNonLiveConfig(CONFIGURATION);
        verify(this.mockDataPersistenceService).getDataBucket(CONFIGURATION);
    }

    @Test
    public void validateNodeSpecificationIsNotEptyWithNonEmptyList() {
        final List<CmSearchScope> listCmSearchScope = new ArrayList<>();
        listCmSearchScope.add(new CmSearchScope());
        this.objUnderTest.validateNodeSpecificationIsNotEmpty(listCmSearchScope);
    }

    @Test
    public void validateNodeSpecificationIsNotEptyWithEmptyList() {
        final List<CmSearchScope> listCmSearchScope = new ArrayList<>();
        this.exception.expect(ValidationException.class);
        this.exception.expectMessage(CONFIG_COPY_NODES_NOT_SPECIFIED_MESSAGE);
        this.objUnderTest.validateNodeSpecificationIsNotEmpty(listCmSearchScope);
    }

    @Test
    public void validateScopeIsNodeNameOrUnspeciedNameWithScopeNodeName() {
        final List<CmSearchScope> listCmSearchScope = new ArrayList<>();
        final CmSearchScope cmSearchScope = new CmSearchScope();
        cmSearchScope.setScopeType(ScopeType.NODE_NAME);
        listCmSearchScope.add(cmSearchScope);

        this.objUnderTest.validateScopeIsNodeNameOrUnspecified(listCmSearchScope);
    }

    @Test
    public void validateScopeIsNodeNameOrUnspeciedNameWithScopeUnspecifed() {
        final List<CmSearchScope> listCmSearchScope = new ArrayList<>();
        final CmSearchScope cmSearchScope = new CmSearchScope();
        cmSearchScope.setScopeType(ScopeType.UNSPECIFIED);
        listCmSearchScope.add(cmSearchScope);

        this.objUnderTest.validateScopeIsNodeNameOrUnspecified(listCmSearchScope);
    }

    @Test
    public void validateScopeIsNodeNameOrUnspecifiedWithNodeWrongScope() {
        final List<CmSearchScope> listCmSearchScope = new ArrayList<>();
        final CmSearchScope cmSearchScope = new CmSearchScope();
        cmSearchScope.setScopeType(ScopeType.FDN);
        listCmSearchScope.add(cmSearchScope);
        this.exception.expect(ValidationException.class);
        this.exception.expectMessage(CONFIG_COPY_NODES_SCOPE_NOT_CORRECT);
        this.objUnderTest.validateScopeIsNodeNameOrUnspecified(listCmSearchScope);
    }

}
