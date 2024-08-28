package com.ericsson.oss.services.cm.cmwriter.ejb;

import static com.ericsson.oss.services.cm.cmtools.error.ErrorHandler.EXECUTION_ERROR;
import static com.ericsson.oss.services.cm.cmtools.error.ErrorHandler.UNEXPECTED_ERROR;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import com.ericsson.oss.itpf.datalayer.dps.DataBucket;
import com.ericsson.oss.itpf.datalayer.dps.DataPersistenceService;
import com.ericsson.oss.itpf.datalayer.dps.exception.general.ObjectNotFoundException;
import com.ericsson.oss.itpf.datalayer.dps.object.builder.ManagedObjectBuilder;
import com.ericsson.oss.itpf.datalayer.dps.object.builder.MibRootBuilder;
import com.ericsson.oss.itpf.datalayer.dps.object.builder.PersistenceObjectBuilder;
import com.ericsson.oss.itpf.datalayer.dps.persistence.ManagedObject;
import com.ericsson.oss.itpf.datalayer.dps.persistence.PersistenceObject;
import com.ericsson.oss.itpf.sdk.core.annotation.EServiceRef;
import com.ericsson.oss.services.cm.cmsearch.CmSearchEngine;
import com.ericsson.oss.services.cm.cmshared.dto.ActionSpecification;
import com.ericsson.oss.services.cm.cmshared.dto.AttributeSpecification;
import com.ericsson.oss.services.cm.cmshared.dto.AttributeSpecifications;
import com.ericsson.oss.services.cm.cmshared.dto.CmObject;
import com.ericsson.oss.services.cm.cmshared.dto.CmObjectSpecification;
import com.ericsson.oss.services.cm.cmshared.dto.CmResponse;
import com.ericsson.oss.services.cm.cmshared.dto.ValidatedAttributeSpecifications;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchCriteria;
import com.ericsson.oss.services.cm.cmtools.dto.mapping.DpsObjectMapper;
import com.ericsson.oss.services.cm.cmtools.error.ErrorHandler;
import com.ericsson.oss.services.cm.cmtools.validation.ValidationService;
import com.ericsson.oss.services.cm.cmtools.validation.ValidationMode;
import com.ericsson.oss.services.cm.cmtools.validation.exceptions.CmValidationException;
import com.ericsson.oss.services.cm.cmwriter.api.CmWriterServiceProxy;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class CmWriterServiceProxyBean implements CmWriterServiceProxy {

    private static final boolean INCLUDE_DESCENDANTS = true;
    private static final boolean DO_NOT_INCLUDE_DESCENDANTS = false;
    private static final String LIVE_BUCKET = "Live";

    @Inject
    private CmSearchEngine cmSearchEngine;

    @EServiceRef
    private DataPersistenceService dataPersistenceService;

    @Inject
    DpsObjectMapper dpsObjectMapper;

    @Inject
    ValidationService validationService;

    @Inject
    ErrorHandler errorHandler;

    Collection<String> fdnList = new ArrayList<String>();
    
    @Override
    @SuppressWarnings("PMD.AvoidCatchingThrowable")
    public CmResponse createNodeRootMib(final CmObjectSpecification cmObjectSpecification) {
    	return createNodeRootMib(cmObjectSpecification,LIVE_BUCKET);
    }

	@Override
	public CmResponse createMibRoot(String parentFdn,
			CmObjectSpecification cmObjectSpecification) {
		return createMibRoot(parentFdn, cmObjectSpecification, LIVE_BUCKET);
	}

	@Override
	public CmResponse createManagedObject(String parentFdn,
			CmObjectSpecification cmObjectSpecification) {
		return createManagedObject(parentFdn, cmObjectSpecification, LIVE_BUCKET);
	}

	@Override
	public CmResponse createPersistenceObject(String namespace, String type,
			String version, Map<String, Object> validatedAttributes) {
		return createPersistenceObject(namespace, type, version, validatedAttributes, LIVE_BUCKET);
	}

	@Override
	public CmResponse setManagedObjectAttributes(String fdn,
			AttributeSpecifications attributeSpecifications) {
		return setManagedObjectAttributes(fdn, attributeSpecifications, LIVE_BUCKET);
	}

	@Override
	public CmResponse setManagedObjectsAttributes(
			CmSearchCriteria cmSearchCriteria,
			AttributeSpecifications attributes) {
		return setManagedObjectsAttributes(cmSearchCriteria, attributes, LIVE_BUCKET);
	}

	@Override
	public CmResponse performAction(String targetFdn,
			ActionSpecification actionSpecification) {
		return performAction(targetFdn, actionSpecification, LIVE_BUCKET);
	}

	@Override
	public CmResponse performAction(CmSearchCriteria cmSearchCriteria,
			ActionSpecification actionSpecification) {
		return performAction(cmSearchCriteria, actionSpecification, LIVE_BUCKET);
	}

	@Override
	public CmResponse deleteManagedObject(String fdn) {
		return deleteManagedObject(fdn, LIVE_BUCKET);
	}

	@Override
	public CmResponse deleteManagedObjectWithDescendants(String fdn) {
		return deleteManagedObjectWithDescendants(fdn, LIVE_BUCKET);
	}

	@Override
	public CmResponse deletePersistenceObjects(CmSearchCriteria cmSearchCriteria) {
		return deletePersistenceObjects(cmSearchCriteria, LIVE_BUCKET);
	}

	@Override
	public CmResponse deletePersistenceObjectsWithDescendants(
			CmSearchCriteria cmSearchCriteria) {
		return deletePersistenceObjectsWithDescendants(cmSearchCriteria, LIVE_BUCKET);
	}
    
    @Override
    @SuppressWarnings("PMD.AvoidCatchingThrowable")
    public CmResponse createNodeRootMib(final CmObjectSpecification cmObjectSpecification, final String bucketName) {
        final String namespace = cmObjectSpecification.getNamespace();
        final CmResponse cmResponse = new CmResponse();
        final String namespaceVersion = cmObjectSpecification.getNamespaceVersion();
        final String type = cmObjectSpecification.getType();
        final AttributeSpecifications attributes = cmObjectSpecification.getAttributeSpecifications();
        final String rootName = cmObjectSpecification.getName();
        try {
            validationService.validateNodeRootMib(namespace, namespaceVersion, type);
            final Map<String, Object> validatedAttributes = convertAttributeSpecificationToMapForDps(validationService.getTypeValidatedAttributes(
                    namespace, namespaceVersion, type, attributes, ValidationMode.WRITE_CREATE));
            final MibRootBuilder mibRootBuilder = createPopulatedMibBuilder(namespace, type, namespaceVersion, rootName, validatedAttributes, bucketName);
            final List<CmObject> cmObjects = createMibRoot(mibRootBuilder);
            populateCmResponse(cmResponse, cmObjects);
        } catch (final CmValidationException e) {
            handleValidationException(cmResponse, e);
        } catch (final Throwable t) {
            handleError(cmResponse, t);
        }
        return cmResponse;
    }


    @Override
    @SuppressWarnings("PMD.AvoidCatchingThrowable")
    public CmResponse createMibRoot(final String parentFdn, final CmObjectSpecification cmObjectSpecification, final String bucketName) {
        final String namespace = cmObjectSpecification.getNamespace();
        final String namespaceVersion = cmObjectSpecification.getNamespaceVersion();
        final String type = cmObjectSpecification.getType();
        final AttributeSpecifications attributes = cmObjectSpecification.getAttributeSpecifications();
        final String rootName = cmObjectSpecification.getName();
        final CmResponse cmResponse = new CmResponse();
        try {
            Map<String, Object> validatedAttributes = null;
            final ManagedObject attachedParentMo = validationService.findManagedObject(parentFdn);
            validationService.validateParentChildRelationship(attachedParentMo, namespace, namespaceVersion, type);
            validatedAttributes = convertAttributeSpecificationToMapForDps(validationService.getTypeValidatedAttributes(namespace, namespaceVersion,
                    type, attributes, ValidationMode.WRITE_CREATE));
            final MibRootBuilder mibRootBuilder = createPopulatedMibBuilder(namespace, type, namespaceVersion, rootName, validatedAttributes, bucketName).parent(
                    attachedParentMo);
            final List<CmObject> cmObjects = createMibRoot(mibRootBuilder);
            populateCmResponse(cmResponse, cmObjects);
        } catch (final CmValidationException e) {
            handleValidationException(cmResponse, e);
        } catch (final Throwable t) {
            handleError(cmResponse, t);
        }
        return cmResponse;
    }

    @Override
    @SuppressWarnings("PMD.AvoidCatchingThrowable")
    public CmResponse createManagedObject(final String parentFdn, final CmObjectSpecification cmObjectSpecification, final String bucketName) {
        final String type = cmObjectSpecification.getType();
        final AttributeSpecifications attributes = cmObjectSpecification.getAttributeSpecifications();
        final String name = cmObjectSpecification.getName();
        final CmResponse cmResponse = new CmResponse();
        try {
            final ManagedObject attachedParentMo = validationService.findManagedObject(parentFdn);
            validationService.validateParentChildRelationship(attachedParentMo, type);
            final Map<String, Object> validatedAttributes = convertAttributeSpecificationToMapForDps(validationService.getTypeValidatedAttributes(
                    attachedParentMo.getNamespace(), attachedParentMo.getVersion(), type, attributes, ValidationMode.WRITE_CREATE));
            final ManagedObjectBuilder ManagedObjectBuilder = createPopulatedManagedObjectBuilder(type, name, validatedAttributes, bucketName).parent(
                    attachedParentMo);
            final List<CmObject> cmObjects = createManagedObject(ManagedObjectBuilder);
            populateCmResponse(cmResponse, cmObjects);
        } catch (final CmValidationException e) {
            handleValidationException(cmResponse, e);
        } catch (final Throwable t) {
            handleError(cmResponse, t);
        }
        return cmResponse;

    }

    @Override
    @SuppressWarnings("PMD.AvoidCatchingThrowable")
    public CmResponse createPersistenceObject(final String namespace, final String type, final String version,
                                              final Map<String, Object> validatedAttributes, final String bucketName) {
        final CmResponse cmResponse = new CmResponse();
        try {
            final PersistenceObjectBuilder persistenceObjectBuilder = createPopulatedPersistenceObjectBuilder(type, namespace, version,
                    validatedAttributes, bucketName);
            final CmObject cmObject = createPersistenceObject(persistenceObjectBuilder);
            populateCmResponse(cmResponse, cmObject);
        } catch (final Throwable t) {
            handleError(cmResponse, t);
        }
        return cmResponse;
    }

    @Override
    @SuppressWarnings("PMD.AvoidCatchingThrowable")
    public CmResponse setManagedObjectAttributes(final String fdn, final AttributeSpecifications attributes, final String bucketName) {
        final CmResponse cmResponse = new CmResponse();
        try {
            final ManagedObject managedObject = validationService.findManagedObject(fdn);
            final Map<String, Object> validatedAttributes = convertAttributeSpecificationToMapForDps(validationService.getTypeValidatedAttributes(
                    managedObject.getNamespace(), managedObject.getVersion(), managedObject.getType(), attributes, ValidationMode.WRITE_SET));
            managedObject.setAttributes(validatedAttributes);
            final CmObject cmObject = dpsObjectMapper.mapToCmObject(managedObject, validatedAttributes.keySet());
            populateCmResponse(cmResponse, cmObject);
        } catch (final CmValidationException e) {
            handleValidationException(cmResponse, e);
        } catch (final Throwable t) {
            handleError(cmResponse, t);
        }
        return cmResponse;
    }

    @Override
    @SuppressWarnings("PMD.AvoidCatchingThrowable")
    public CmResponse setManagedObjectsAttributes(final CmSearchCriteria cmSearchCriteria, final AttributeSpecifications attributes, final String bucketName) {
        final CmResponse returnCmResponse = new CmResponse();
        try {
            final Collection<CmObject> cmObjects = cmSearchEngine.search(cmSearchCriteria, (List)cmSearchCriteria.getCmObjectSpecifications());
            final List<CmObject> modifiedCmObjects = new ArrayList<CmObject>();
            for (final CmObject cmObject : cmObjects) {
                final ManagedObject managedObject = validationService.findManagedObject(cmObject.getFdn());
                final Map<String, Object> validatedAttributes = convertAttributeSpecificationToMapForDps(validationService
                        .getTypeValidatedAttributes(managedObject.getNamespace(), managedObject.getVersion(), managedObject.getType(), attributes,
                                ValidationMode.WRITE_SET));
                managedObject.setAttributes(validatedAttributes);
                final CmObject mappedCmObject = dpsObjectMapper.mapToCmObject(managedObject, validatedAttributes.keySet());
                modifiedCmObjects.add(mappedCmObject);
            }
            populateCmResponse(returnCmResponse, modifiedCmObjects);
        } catch (final CmValidationException e) {
            handleValidationException(returnCmResponse, e);
        } catch (final Throwable t) {
            handleError(returnCmResponse, t);
        }
        return returnCmResponse;

    }

    @Override
    public CmResponse deleteManagedObject(final String fdn, final String bucketName) {
        return deleteMoByFdn(fdn, DO_NOT_INCLUDE_DESCENDANTS, bucketName);
    }

    @Override
    public CmResponse deleteManagedObjectWithDescendants(final String fdn, final String bucketName) {
        return deleteMoByFdn(fdn, INCLUDE_DESCENDANTS, bucketName);
    }

    @Override
    public CmResponse deletePersistenceObjects(final CmSearchCriteria cmSearchCriteria, final String bucketName) {
        return deletePersistenceObjects(cmSearchCriteria, DO_NOT_INCLUDE_DESCENDANTS, bucketName);
    }

    // TODO eqruaae, will ignore -ALL at the moment, it will be modified when we can get the child's mo with query
    @Override
    public CmResponse deletePersistenceObjectsWithDescendants(final CmSearchCriteria cmSearchCriteria, final String bucketName) {
        return deletePersistenceObjects(cmSearchCriteria, DO_NOT_INCLUDE_DESCENDANTS, bucketName);
    }

    @Override
    @SuppressWarnings("PMD.AvoidCatchingThrowable")
    public CmResponse performAction(final String targetFdn, final ActionSpecification actionSpecification, final String bucketName) {

        final CmResponse cmResponse = new CmResponse();

        try {

            final ManagedObject managedObject = validationService.findManagedObject(targetFdn);
            performActionOnPo(actionSpecification, managedObject);
            final List<CmObject> cmObjects = new ArrayList(1);
            cmObjects.add(dpsObjectMapper.mapToCmObject(managedObject, DpsObjectMapper.INCLUDE_ALL_ATTRIBUTES));
            populateCmResponseForAction(cmResponse, cmObjects);

        } catch (final CmValidationException e) {
            handleValidationException(cmResponse, e);
        } catch (final Throwable t) {
            handleError(cmResponse, t);
        }

        return cmResponse;
    }

    @Override
    @SuppressWarnings("PMD.AvoidCatchingThrowable")
    public CmResponse performAction(final CmSearchCriteria cmSearchCriteria, final ActionSpecification actionSpecification, final String bucketName) {
        final CmResponse returnCmResponse = new CmResponse();
        try {
            final List<CmObject> cmObjects = cmSearchEngine.search(cmSearchCriteria, (List)cmSearchCriteria.getCmObjectSpecifications());
            for (final CmObject cmObject : cmObjects) {
                final PersistenceObject persistenceObject = getDPSBucket(bucketName).findPoById(cmObject.getPoId());
                performActionOnPo(actionSpecification, persistenceObject);
            }
            populateCmResponseForAction(returnCmResponse, cmObjects);
        } catch (final CmValidationException e) {
            handleValidationException(returnCmResponse, e);
        } catch (final Throwable t) {
            handleError(returnCmResponse, t);
        }

        return returnCmResponse;
    }

    /*
     * P R I V A T E - M E T H O D S
     */

    private void handleError(final CmResponse cmResponse, final Throwable t) {
        final String errorMessage = errorHandler.createErrorMessageFromException(t);
        cmResponse.setStatusCode(UNEXPECTED_ERROR);
        cmResponse.setStatusMessage(errorMessage);
    }

    private void handleValidationException(final CmResponse cmResponse, final CmValidationException e) {
        cmResponse.setStatusCode(EXECUTION_ERROR);
        cmResponse.setStatusMessage(e.getMessage());
    }

    private void populateCmResponse(final CmResponse cmResponse, final CmObject cmObject) {
        final List<CmObject> cmObjects = new ArrayList<>(1);
        cmObjects.add(cmObject);
        populateCmResponse(cmResponse, cmObjects);
    }

    private void populateCmResponse(final CmResponse cmResponse, final List<CmObject> cmObjects) {
        cmResponse.setCmObjects(cmObjects);
        final int responseSize = cmObjects.size();
        cmResponse.setStatusCode(responseSize);
        cmResponse.setStatusMessage(responseSize + " instance(s) updated");
    }

    private void populateCmResponseForAction(final CmResponse cmResponse, final List<CmObject> cmObjects) {
        cmResponse.setCmObjects(cmObjects);
        cmResponse.setStatusCode(cmObjects.size());
        cmResponse.setStatusMessage(cmObjects.size() + " instance(s)");
    }

    private void populateCmResponseForDelete(final CmResponse cmResponse, final int numberOfDeleted, final Collection<String> fdnList) {
        if (numberOfDeleted > 0) {
            cmResponse.setStatusCode(numberOfDeleted);
            cmResponse.setStatusMessage("The following " + numberOfDeleted + " instance(s) have been deleted: " + fdnList);
        }
    }

    private List<CmObject> createMibRoot(final MibRootBuilder mibRootBuilder) {
        final List<CmObject> cmObjects = new ArrayList<>();
        cmObjects.add(dpsObjectMapper.mapToCmObject(mibRootBuilder.create()));
        return cmObjects;
    }

    private MibRootBuilder createPopulatedMibBuilder(final String namespace, final String type, final String namespaceVersion, final String rootName,
                                                     final Map<String, Object> attributes, final String bucketName) {
        final DataBucket liveBucket = getDPSBucket(bucketName);
        return liveBucket.getMibRootBuilder().namespace(namespace).type(type).version(namespaceVersion).name(rootName).addAttributes(attributes);

    }

	/**
	 * @return
	 */
	private DataBucket getDPSBucket(final String bucketName) {
		if (bucketName.equalsIgnoreCase(LIVE_BUCKET)) {
			return dataPersistenceService.getLiveBucket();
		}
		else {
	        DataBucket configBucket = null;
	        try {
	                configBucket = dataPersistenceService.getDataBucket(bucketName);
	        } catch (ObjectNotFoundException onfe) {
	                configBucket = dataPersistenceService.createDataBucket(bucketName, bucketName);
	        }
			return configBucket;
		}
	}

    private List<CmObject> createManagedObject(final ManagedObjectBuilder managedObjectBuilder) {
        final List<CmObject> cmObjects = new ArrayList<>();
        cmObjects.add(dpsObjectMapper.mapToCmObject(managedObjectBuilder.create()));
        return cmObjects;
    }

    private ManagedObjectBuilder createPopulatedManagedObjectBuilder(final String type, final String name, final Map<String, Object> attributes, final String bucketName) {
        final DataBucket liveBucket = getDPSBucket(bucketName);
        return liveBucket.getManagedObjectBuilder().type(type).name(name).addAttributes(attributes);
    }

    private PersistenceObjectBuilder createPopulatedPersistenceObjectBuilder(final String type, final String namespace, final String version,
                                                                             final Map<String, Object> attributes, final String bucketName) {
        final DataBucket liveBucket = getDPSBucket(bucketName);
        return liveBucket.getPersistenceObjectBuilder().type(type).namespace(namespace).version(version).addAttributes(attributes);
    }

    private CmObject createPersistenceObject(final PersistenceObjectBuilder persistenceObjectBuilder) {
        return dpsObjectMapper.mapToCmObject(persistenceObjectBuilder.create());
    }

    private Map<String, Object> convertAttributeSpecificationToMapForDps(final ValidatedAttributeSpecifications validatedAttributeSpecifications) {
        final Map<String, Object> result = new HashMap<>(validatedAttributeSpecifications.size());
        for (final String name : validatedAttributeSpecifications.getAttributeNames()) {
            final AttributeSpecification attributeSpecification = validatedAttributeSpecifications.getAttributeSpecification(name);
            result.put(attributeSpecification.getName(), attributeSpecification.getValue());
        }
        return result;
    }

    @SuppressWarnings("PMD.AvoidCatchingThrowable")
    private CmResponse deleteMoByFdn(final String fdn, final boolean includeDescendants, final String bucketName) {
        final CmResponse cmResponse = new CmResponse();
        try {
            final ManagedObject managedObject = validationService.findManagedObject(fdn);
            if (!includeDescendants) {
                validationService.ensureManagedObjectHasNoChildren(managedObject);
            }
            final int numberOfDeletedMOs = deleteManagedObject(managedObject, bucketName);
            fdnList.add(fdn + "\n");
            populateCmResponseForDelete(cmResponse, numberOfDeletedMOs, fdnList);
        } catch (final CmValidationException e) {
            handleValidationException(cmResponse, e);
        } catch (final Throwable t) {
            handleError(cmResponse, t);
        }
        return cmResponse;
    }

    @SuppressWarnings("PMD.AvoidCatchingThrowable")
    private CmResponse deletePersistenceObjects(final CmSearchCriteria cmSearchCriteria, final boolean includeDescendants, final String bucketName) {
        CmResponse cmResponse = new CmResponse();
        int numberOfDeleted = 0;
        final Collection<CmObject> cmObjects = cmSearchEngine.search(cmSearchCriteria, (List) cmSearchCriteria.getCmObjectSpecifications());
        for (final CmObject cmObject : cmObjects) {
            final String fdn = cmObject.getFdn();
            if (fdn != null) {
                cmResponse = deleteMoByFdn(fdn, includeDescendants, bucketName);
                numberOfDeleted += cmResponse.getStatusCode();
                fdnList.add(fdn + "\n");
            } else {
                deletePersistanceObject(cmObject.getPoId(), bucketName);
                numberOfDeleted++;
            }
        }
        populateCmResponseForDelete(cmResponse, numberOfDeleted, fdnList);
        return cmResponse;
    }

    private int deleteManagedObject(final ManagedObject managedObject, final String bucketName) {
        final DataBucket liveBucket = getDPSBucket(bucketName);
        return liveBucket.deletePo(managedObject);
    }

    private void deletePersistanceObject(final long poId, final String bucketName) {
        final DataBucket liveBucket = getDPSBucket(bucketName);
        final PersistenceObject persistenceObject = liveBucket.findPoById(poId);
        liveBucket.deletePo(persistenceObject);
    }

    private void performActionOnPo(final ActionSpecification actionSpecification, final PersistenceObject persistenceObject) {
        //TODO emcgdan:- when attributes are introduced validation should take place
        //TODO HashMap will be removed when action args are introduced
        persistenceObject.performAction(actionSpecification.getActionName(), new HashMap<String, Object>(0));
    }
    
}