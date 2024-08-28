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
package com.ericsson.oss.services.cm.cmcompare.ejb;

import static com.ericsson.oss.services.cm.cmtools.error.ErrorHandler.*;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import com.ericsson.oss.itpf.datalayer.dps.DataPersistenceService;
import com.ericsson.oss.itpf.sdk.core.annotation.EServiceRef;
import com.ericsson.oss.services.cm.cmcompare.api.CmCompareService;
import com.ericsson.oss.services.cm.cmcompare.api.CmConfigDiffParams;
import com.ericsson.oss.services.cm.cmcompare.api.CmConfigUpdateParams;
import com.ericsson.oss.services.cm.cmshared.dto.CmObjectSpecification;
import com.ericsson.oss.services.cm.cmshared.dto.CmObject;
import com.ericsson.oss.services.cm.cmshared.dto.CmResponse;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchCriteria;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchScope;
import com.ericsson.oss.services.cm.cmtools.dto.mapping.DpsObjectMapper;
import com.ericsson.oss.services.cm.cmtools.error.ErrorHandler;
import com.ericsson.oss.services.cm.cmtools.validation.ValidationService;
import com.ericsson.oss.services.cm.cmtools.validation.exceptions.CmValidationException;
import com.ericsson.oss.services.cm.cmwriter.api.CmWriterServiceProxy;

/**
 * cm configuration service bean implementing the business logic for cm configuration api's.
 * 
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class CmCompareServiceBean implements CmCompareService {

    private static final String DEFAULT_SPECIFICATION_TYPE = "MeContext";
    private static final String DEFAULT_SPECIFICATION_NAMESPACE = "OSS_TOP";
    private static final int UNIMPLEMENTED = -1;

    @Inject
    ValidationService validationService;

    @Inject
    DpsObjectMapper dpsObjectMapper;

    @Inject
    ErrorHandler errorHandler;

    @EServiceRef
    private DataPersistenceService dataPersistenceService;

    @EServiceRef
    private CmWriterServiceProxy cmWriterService;

    @Override
    @SuppressWarnings("PMD.AvoidCatchingThrowable")
    public CmResponse createConfig(final String configName) {
        final CmResponse returnCmResponse = new CmResponse();
        try {
            this.dataPersistenceService.createDataBucket(configName, configName);
        } catch (final Throwable t) {
            handleError(returnCmResponse, t);
        }
        return returnCmResponse;
    }

    @Override
    @SuppressWarnings("PMD.AvoidCatchingThrowable")
    public CmResponse listConfig() {
        final CmResponse returnCmResponse = new CmResponse();
        final Collection<String> configs;
        try {
            configs = this.dataPersistenceService.getAllDataBucketNames();
            populateCmResponse(returnCmResponse, mapConfigToCmObject(configs));
        } catch (final Throwable t) {
            handleError(returnCmResponse, t);
        }
        return returnCmResponse;
    }

    @Override
    @SuppressWarnings("PMD.AvoidCatchingThrowable")
    public CmResponse deleteConfig(String configName) {
        final CmResponse returnCmResponse = new CmResponse();
        try {
            this.dataPersistenceService.deleteDataBucket(configName, true);
        } catch (final Throwable t) {
            handleError(returnCmResponse, t);
        }
        return returnCmResponse;
    }

    @Override
    @SuppressWarnings("PMD.AvoidCatchingThrowable")
    public CmResponse createConfig(final String configName, final CmSearchCriteria criteria) {
        CmResponse cmResponse = new CmResponse();
        final Iterator<CmSearchScope> scopes = criteria.getCmSearchScopes().iterator();

        while (scopes.hasNext()) {

            final String parentFdn = scopes.next().getValue();
            final CmObjectSpecification cmObjectSpecification = criteria.getCmObjectSpecifications().iterator().next();

            if (isChildManagedObject(cmObjectSpecification)) {
                cmResponse = createChildManagedObject(parentFdn, cmObjectSpecification, configName);
            } else if (isMeContext(parentFdn, cmObjectSpecification)) {
                cmResponse = createMeContext(cmObjectSpecification, configName);
            } else {
                cmResponse = createMibRootWithParent(parentFdn, cmObjectSpecification, configName);
            }
            if (cmResponse.getStatusCode() < 0) {
                break;
            }
        }
        return cmResponse;
    }

    private boolean isChildManagedObject(final CmObjectSpecification cmObjectSpecification) {
        return cmObjectSpecification.getNamespace() == null && cmObjectSpecification.getNamespaceVersion() == null;
    }

    private boolean isMeContext(final String parentFdn, final CmObjectSpecification cmObjectSpecification) {
        return parentFdn == null && cmObjectSpecification.getNamespace() != null && cmObjectSpecification.getNamespaceVersion() != null;
    }

    private CmResponse createMeContext(final CmObjectSpecification cmObjectSpecification, final String configName) {
        return this.cmWriterService.createNodeRootMib(cmObjectSpecification, configName);
    }

    private CmResponse createChildManagedObject(final String parentFdn, final CmObjectSpecification cmObjectSpecification, final String configName) {
        return this.cmWriterService.createManagedObject(parentFdn, cmObjectSpecification, configName);
    }

    private CmResponse createMibRootWithParent(final String parentFdn, final CmObjectSpecification cmObjectSpecification, final String configName) {
        return this.cmWriterService.createMibRoot(parentFdn, cmObjectSpecification, configName);
    }

    @Override
    public CmResponse updateConfig(String toConfigName, String fromConfigName, CmSearchCriteria scope, CmConfigUpdateParams params) {
        final CmResponse cmResponse = new CmResponse();
        cmResponse.setStatusCode(UNIMPLEMENTED);
        return cmResponse;
    }

    @Override
    public CmResponse diffConfig(String toConfigName, String fromConfigName, CmSearchCriteria scope, CmConfigDiffParams params) {
        final CmResponse cmResponse = new CmResponse();
        cmResponse.setStatusCode(UNIMPLEMENTED);
        return cmResponse;
    }

    @Override
    public CmResponse linkConfig(String overlayConfigName, String baseConfigName) {
        final CmResponse cmResponse = new CmResponse();
        cmResponse.setStatusCode(UNIMPLEMENTED);
        return cmResponse;
    }

    @Override
    public CmResponse unlinkConfig(String overlayConfigName) {
        final CmResponse cmResponse = new CmResponse();
        cmResponse.setStatusCode(UNIMPLEMENTED);
        return cmResponse;
    }

    @Override
    public CmResponse lockConfig(String configName, String reasonString) {
        final CmResponse cmResponse = new CmResponse();
        cmResponse.setStatusCode(UNIMPLEMENTED);
        return cmResponse;
    }

    @Override
    public CmResponse unlockConfig(String configName) {
        final CmResponse cmResponse = new CmResponse();
        cmResponse.setStatusCode(UNIMPLEMENTED);
        return cmResponse;
    }

    /**
     * Helper methods.
     */
    private void handleError(final CmResponse cmResponse, final Throwable t) {
        final String errorMessage = this.errorHandler.createErrorMessageFromException(t);
        cmResponse.setStatusCode(UNEXPECTED_ERROR);
        cmResponse.setStatusMessage(errorMessage);
    }

    private void handleValidationException(final CmResponse cmResponse, final CmValidationException e) {
        cmResponse.setStatusCode(EXECUTION_ERROR);
        cmResponse.setStatusMessage(e.getMessage());
    }

    private void populateCmResponse(final CmResponse cmResponse, final List<CmObject> cmObjects) {
        cmResponse.setCmObjects(cmObjects);
        final int responseSize = cmObjects.size();
        cmResponse.setStatusCode(responseSize);
        cmResponse.setStatusMessage(responseSize + " instance(s) updated");
    }

    /**
     * create a default CmObjectSpecifcation.
     * 
     * @return CmObjectSpecification
     */
    protected CmObjectSpecification createDefaultCmObjectSpecifcation() {
        final CmObjectSpecification defaultCmObjectSpeecification;
        defaultCmObjectSpeecification = new CmObjectSpecification();
        defaultCmObjectSpeecification.setAttributeSpecifications(CmObjectSpecification.ALL_ATTRIBUTES);
        defaultCmObjectSpeecification.setNamespace(DEFAULT_SPECIFICATION_NAMESPACE);
        defaultCmObjectSpeecification.setType(DEFAULT_SPECIFICATION_TYPE);
        return defaultCmObjectSpeecification;
    }

    private List<CmObject> mapConfigToCmObject(final Collection<String> configNames) {
        final List<CmObject> cmObjects = new ArrayList<>();
        final Iterator<String> nameIterator = configNames.iterator();
        while (nameIterator.hasNext()) {
            final CmObject cmObject = new CmObject();
            cmObject.setName(nameIterator.next());
            cmObjects.add(cmObject);
        }
        return cmObjects;
    }

}
