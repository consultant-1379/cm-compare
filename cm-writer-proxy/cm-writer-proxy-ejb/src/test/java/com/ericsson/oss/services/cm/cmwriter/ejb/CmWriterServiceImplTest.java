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
package com.ericsson.oss.services.cm.cmwriter.ejb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.ericsson.oss.itpf.datalayer.dps.DataBucket;
import com.ericsson.oss.itpf.datalayer.dps.DataPersistenceService;
import com.ericsson.oss.itpf.datalayer.dps.object.builder.ManagedObjectBuilder;
import com.ericsson.oss.itpf.datalayer.dps.object.builder.MibRootBuilder;
import com.ericsson.oss.itpf.datalayer.dps.object.builder.PersistenceObjectBuilder;
import com.ericsson.oss.itpf.datalayer.dps.persistence.ManagedObject;
import com.ericsson.oss.itpf.datalayer.dps.persistence.PersistenceObject;
import com.ericsson.oss.services.cm.cmsearch.CmSearchEngine;
import com.ericsson.oss.services.cm.cmshared.dto.ActionSpecification;
import com.ericsson.oss.services.cm.cmshared.dto.AttributeSpecification;
import com.ericsson.oss.services.cm.cmshared.dto.AttributeSpecifications;
import com.ericsson.oss.services.cm.cmshared.dto.CmObject;
import com.ericsson.oss.services.cm.cmshared.dto.CmObjectSpecification;
import com.ericsson.oss.services.cm.cmshared.dto.CmResponse;
import com.ericsson.oss.services.cm.cmshared.dto.ValidatedAttributeSpecifications;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchCriteria;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchScope;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchScope.ScopeType;
import com.ericsson.oss.services.cm.cmtools.dto.mapping.DpsObjectMapper;
import com.ericsson.oss.services.cm.cmtools.error.ErrorHandler;
import com.ericsson.oss.services.cm.cmtools.error.ErrorHandlerImpl;
import com.ericsson.oss.services.cm.cmtools.validation.ValidationMode;
import com.ericsson.oss.services.cm.cmtools.validation.ValidationService;
import com.ericsson.oss.services.cm.cmtools.validation.exceptions.CmValidationException;

@Ignore
@RunWith(MockitoJUnitRunner.class)
public class CmWriterServiceImplTest {
    @Mock
    @Inject
    private DataPersistenceService dataPersistenceService;

    @Mock
    @Inject
    DpsObjectMapper dpsObjectMapper;

    @Mock
    @Inject
    ValidationService validationService;

    @Mock
    @Inject
    ErrorHandlerImpl errorHandlerImpl;

    @Mock
    @Inject
    CmSearchEngine cmSearchEngine;

    @InjectMocks
    // this will do all the magic!! Mockito will inject your mocked session
    // façade into the appropriate field … so no need for a @Before method
    CmWriterServiceProxyBean objUnderTest = new CmWriterServiceProxyBean();

    final String fdn = "some fdn";
    final String namespace = "some namespace";
    final String namespaceVersion = "1.2.3";
    final String type = "some Type";
    final String name = "some Name";
    final Map<String, Object> validatedAttributes = new HashMap<String, Object>(); // No content needed for jUnit test of ServiceImpl!
    final Map<String, Object> moAttributes = new HashMap<String, Object>(); // No content needed for jUnit test of ServiceImpl!
    final ValidatedAttributeSpecifications validatedAttributeSpecifications = new ValidatedAttributeSpecifications();
    final Object actionObject = new Object();
    final String actionName = "someAction";
    final Map<String, Object> actionAttributes = new HashMap<>(0);
    final CmSearchCriteria cmSearchCriteria = createCmSearchCriteria();

    @Mock
    AttributeSpecifications attributeSpecifications;
    @Mock
    MibRootBuilder mibRootBuilder;
    @Mock
    ManagedObjectBuilder managedObjectBuilder;
    @Mock
    PersistenceObjectBuilder persistenceObjectBuilder;
    @Mock
    DataBucket liveBucket;
    @Mock
    ManagedObject managedObject;
    @Mock
    PersistenceObject persistenceObject;
    @Mock
    CmObject cmObject;
    CmObjectSpecification cmObjectSpecification;

    Collection<ManagedObject> childrenList = new ArrayList<ManagedObject>();

    List<CmObject> cmObjects = new ArrayList<>();

    @Before
    public void setupCmObjectSpecification() {
        cmObjectSpecification = createCmObjectSpecification(namespace, namespaceVersion, type, name, attributeSpecifications);
        final AttributeSpecification attributeSpecification = new AttributeSpecification();
        attributeSpecification.setName("x2BlackList");
        attributeSpecification.setValue("[(enbId=1,mcc=1,mnc=123,mncLength=2)]");
        validatedAttributeSpecifications.addAttributeSpecification(attributeSpecification);
    }

    @Test
    public void createNodeRootMibReturnsSuccessWhenValidationFine() {
        setupMibRootMocks();
        when(validationService.getTypeValidatedAttributes(namespace, namespaceVersion, type, attributeSpecifications, ValidationMode.WRITE_CREATE))
                .thenReturn(validatedAttributeSpecifications);
        // Execute the test:
        final CmResponse result = objUnderTest.createNodeRootMib(cmObjectSpecification);
        assertEquals(1, result.getStatusCode());
        assertEquals(cmObject, result.getCmObjects().iterator().next());
    }

    @Test
    public void createNodeRootMibReturnsExecutionErrorWhenValidationThrowsCmValidationException() {
        setupMibRootMocks();
        final String errorMessage = "x";
        when(validationService.getTypeValidatedAttributes(namespace, namespaceVersion, type, attributeSpecifications, ValidationMode.WRITE_CREATE))
                .thenThrow(new CmValidationException(errorMessage));
        // Execute the test:
        final CmResponse result = objUnderTest.createNodeRootMib(cmObjectSpecification);
        assertEquals(errorMessage, result.getStatusMessage());
        assertEquals(-1, result.getStatusCode());
    }

    @Test
    public void createNodeRootMibReturnsUnhandledErrorWhenValidationThrowsRuntimeException() {
        setupMibRootMocks();
        final String errorMessage = "x";
        when(validationService.getTypeValidatedAttributes(namespace, namespaceVersion, type, attributeSpecifications, ValidationMode.WRITE_CREATE))
                .thenThrow(new RuntimeException(errorMessage));
        // Execute the test:
        final CmResponse result = objUnderTest.createNodeRootMib(cmObjectSpecification);
        assertEquals(ErrorHandler.UNEXPECTED_ERROR, result.getStatusCode());
    }

    @Test
    public void createMibRootReturnsSuccessWhenValidationFine() {
        setupMibRootMocks();
        when(validationService.getTypeValidatedAttributes(namespace, namespaceVersion, type, attributeSpecifications, ValidationMode.WRITE_CREATE))
                .thenReturn(validatedAttributeSpecifications);
        final CmResponse result = objUnderTest.createMibRoot(fdn, cmObjectSpecification);
        assertEquals(1, result.getStatusCode());
        assertEquals(cmObject, result.getCmObjects().iterator().next());
    }

    @Test
    public void createMibRootReturnsExecutionErrorWhenValidationThrowsCmValidationException() {
        setupMibRootMocks();
        final String errorMessage = "x";
        when(validationService.getTypeValidatedAttributes(namespace, namespaceVersion, type, attributeSpecifications, ValidationMode.WRITE_CREATE))
                .thenThrow(new CmValidationException(errorMessage));
        // Execute the test:
        final CmResponse result = objUnderTest.createMibRoot(fdn, cmObjectSpecification);
        assertEquals(errorMessage, result.getStatusMessage());
        assertEquals(-1, result.getStatusCode());
    }

    @Test
    public void createMibRootMibReturnsUnhandledErrorWhenValidationThrowsRuntimeException() {
        setupMibRootMocks();

        final String errorMessage = "x";
        when(validationService.getTypeValidatedAttributes(namespace, namespaceVersion, type, attributeSpecifications, ValidationMode.WRITE_CREATE))
                .thenThrow(new RuntimeException(errorMessage));
        // Execute the test:
        final CmResponse result = objUnderTest.createMibRoot(fdn, cmObjectSpecification);
        assertEquals(ErrorHandler.UNEXPECTED_ERROR, result.getStatusCode());
    }

    @Test
    public void createManagedObjectReturnsSuccessWhenValidationFine() {
        setupManagedObjectMocks();
        when(validationService.getTypeValidatedAttributes(namespace, namespaceVersion, type, attributeSpecifications, ValidationMode.WRITE_CREATE))
                .thenReturn(validatedAttributeSpecifications);
        // Execute the test:
        final CmResponse result = objUnderTest.createManagedObject(fdn, cmObjectSpecification);
        assertEquals(1, result.getStatusCode());
        assertEquals(cmObject, result.getCmObjects().iterator().next());
    }

    @Test
    public void createManagedObjectReturnsCmObject() {
        setupManagedObjectMocks();
        when(validationService.getTypeValidatedAttributes(namespace, namespaceVersion, type, attributeSpecifications, ValidationMode.WRITE_CREATE))
                .thenReturn(validatedAttributeSpecifications);
        // Execute the test:
        final CmResponse result = objUnderTest.createManagedObject(fdn, cmObjectSpecification);
        assertEquals(1, result.getStatusCode());
        assertEquals(cmObject, result.getCmObjects().iterator().next());
    }

    @Test
    public void createManagedObjectReturnsExecutionErrorWhenValidationThrowsCmValidationException() {
        setupManagedObjectMocks();
        final String errorMessage = "x";
        when(validationService.findManagedObject(fdn)).thenThrow(new CmValidationException(errorMessage));
        // Execute the test:
        final CmResponse result = objUnderTest.createManagedObject(fdn, cmObjectSpecification);
        assertEquals(errorMessage, result.getStatusMessage());
        assertEquals(-1, result.getStatusCode());
    }

    @Test
    public void createManagedObjectReturnsUnhandledErrorWhenValidationThrowsRuntimeException() {
        setupManagedObjectMocks();
        final String errorMessage = "x";
        when(validationService.parentExists(fdn)).thenThrow(new RuntimeException(errorMessage));

        // Execute the test:
        final CmResponse result = objUnderTest.createManagedObject(fdn, cmObjectSpecification);
        assertEquals(ErrorHandler.UNEXPECTED_ERROR, result.getStatusCode());
    }

    @Test
    public void createPersistenceObjectReturnsCorrectCmPersistenceObject() {
        setupPersistenceObjectMocks();
        // Execute the test:
        final CmResponse result = objUnderTest.createPersistenceObject(namespace, type, namespaceVersion, validatedAttributes);
        assertEquals(1, result.getStatusCode());
        final CmObject createdPersistenceObject = result.getCmObjects().iterator().next();
        assertEquals(cmObject, createdPersistenceObject);
    }

    @Test
    public void createPersistenceObjectReturnsErrorCode() {
        final RuntimeException runtimeException = new RuntimeException("error Message");
        when(dataPersistenceService.getLiveBucket()).thenThrow(runtimeException);
        // Execute the test:
        final CmResponse result = objUnderTest.createPersistenceObject(namespace, type, namespaceVersion, validatedAttributes);
        assertEquals(ErrorHandler.UNEXPECTED_ERROR, result.getStatusCode());
    }

    @Test
    public void createPersistenceObjectWithComplexAttributesReturnsSuccess() {
        setupPersistenceObjectMocks();
        final List<Long> moIdList = new ArrayList<>();
        moIdList.add(1L);
        validatedAttributes.put("moList", moIdList);
        // Execute the test:
        final CmResponse result = objUnderTest.createPersistenceObject(namespace, type, namespaceVersion, validatedAttributes);
        assertEquals(1, result.getStatusCode());
        final CmObject createdCmObject = result.getCmObjects().iterator().next();
        assertEquals(cmObject, createdCmObject);
    }

    @Test
    public void canSetManagedObjectAttributes() {
        setupManagedObjectMocks();
        final String fdn = "MeContext=ERBS01";
        addAttributeSpecifications();
        when(validationService.findManagedObject(fdn)).thenReturn(managedObject);
        when(validationService.getTypeValidatedAttributes(namespace, namespaceVersion, type, attributeSpecifications, ValidationMode.WRITE_SET))
                .thenReturn(validatedAttributeSpecifications);
        final CmResponse cmResponse = objUnderTest.setManagedObjectAttributes(fdn, attributeSpecifications);
        assertEquals(1, cmResponse.getStatusCode());
        assertEquals(cmObject, cmResponse.getCmObjects().iterator().next());
    }

    @Test
    public void setManagedObjectReturnsMessageFromValidationServiceWhenValidationFails() {
        setupManagedObjectMocks();
        final String errorMessage = "X";
        when(validationService.findManagedObject(anyString())).thenThrow(new CmValidationException(errorMessage));
        final CmResponse result = objUnderTest.setManagedObjectAttributes("fdn", null);
        assertEquals(errorMessage, result.getStatusMessage());
        assertEquals(-1, result.getStatusCode());
    }

    @Test
    public void setManagedObjectReturnsExecutionErrorWhenValidationThrowsCmValidationException() {
        setupManagedObjectMocks();
        final String errorMessage = "X";
        final String fdn = "MeContext=ERBS01";
        when(validationService.findManagedObject(fdn)).thenThrow(new RuntimeException(errorMessage));
        final CmResponse result = objUnderTest.setManagedObjectAttributes(fdn, null);
        assertEquals(ErrorHandler.UNEXPECTED_ERROR, result.getStatusCode());
    }

    @Test
    public void canDeleteMoByFdn() {
        setupManagedObjectMocks();
        final String fdn = "MeContext=ERBS01";
        when(validationService.findManagedObject(fdn)).thenReturn(managedObject);
        final CmResponse cmResponse = objUnderTest.deleteManagedObject(fdn);
        assertEquals(1, cmResponse.getStatusCode());
        assertTrue(cmResponse.getStatusMessage().contains(fdn));
    }

    @Test
    public void deleteMoByFdnReturnsMessageFromValidationServiceWhenValidationFails() {
        setupManagedObjectMocks();
        final String errorMessage = "X";
        when(validationService.findManagedObject(anyString())).thenThrow(new CmValidationException(errorMessage));
        final CmResponse result = objUnderTest.deleteManagedObject(fdn);
        assertEquals(errorMessage, result.getStatusMessage());
        assertEquals(-1, result.getStatusCode());
    }

    @Test
    public void deleteMoByFdnExecutionErrorWhenValidationThrowsCmValidationException() {
        setupManagedObjectMocks();
        final String errorMessage = "X";
        final String fdn = "MeContext=ERBS01";
        when(validationService.findManagedObject(fdn)).thenThrow(new RuntimeException(errorMessage));
        final CmResponse result = objUnderTest.deleteManagedObject(fdn);
        assertEquals(ErrorHandler.UNEXPECTED_ERROR, result.getStatusCode());
    }

    @Test
    public void canDeleteMoTreeByFdnUsingAllParameter() {
        setupManagedObjectMocks();
        final String fdn = "MeContext=ERBS01";
        when(validationService.findManagedObject(fdn)).thenReturn(managedObject);
        final CmResponse cmResponse = objUnderTest.deleteManagedObjectWithDescendants(fdn);
        assertEquals(1, cmResponse.getStatusCode());
        assertTrue(cmResponse.getStatusMessage().contains(fdn));
    }

    @Test
    public void canDeleteMoWithNoChildByFdn() {
        setupManagedObjectMocks();
        final String fdn = "MeContext=ERBS01";
        when(validationService.findManagedObject(fdn)).thenReturn(managedObject);
        final CmResponse cmResponse = objUnderTest.deleteManagedObjectWithDescendants(fdn);
        assertEquals(1, cmResponse.getStatusCode());
        assertTrue(cmResponse.getStatusMessage().contains(fdn));
    }

    @Test
    public void deleteMoTreeByFdnReturnsMessageFromValidationServiceWhenValidationFails() {
        setupManagedObjectMocks();
        final String errorMessage = "X";
        when(validationService.findManagedObject(anyString())).thenThrow(new CmValidationException(errorMessage));
        final CmResponse result = objUnderTest.deleteManagedObjectWithDescendants(fdn);
        assertEquals(errorMessage, result.getStatusMessage());
        assertEquals(-1, result.getStatusCode());
    }

    @Test
    public void deleteMotreeByFdnExecutionErrorWhenValidationThrowsCmValidationException() {
        setupManagedObjectMocks();
        final String errorMessage = "X";
        final String fdn = "MeContext=ERBS01";
        when(validationService.findManagedObject(fdn)).thenThrow(new RuntimeException(errorMessage));
        final CmResponse result = objUnderTest.deleteManagedObjectWithDescendants(fdn);
        assertEquals(ErrorHandler.UNEXPECTED_ERROR, result.getStatusCode());
    }

    @Test
    public void canDeletePersistenceObjectsWhenSearchResultsReturnsObjectsWithFdns() {
        setupManagedObjectMocks();
        mockSearchResult("FDN");
        final CmResponse cmResponse = objUnderTest.deletePersistenceObjects(cmSearchCriteria);
        assertEquals("Dit not get expected status code and message was " + cmResponse.getStatusMessage(), 1, cmResponse.getStatusCode());
        //        assertTrue(cmResponse.getStatusMessage().contains("FDN"));
    }

    @Test
    public void canDeletePersistenceObjectsWhenSearchResultsReturnsObjectsWithoutFdns() {
        setupManagedObjectMocks();
        mockSearchResult(123l);
        final CmResponse cmResponse = objUnderTest.deletePersistenceObjects(cmSearchCriteria);
        assertEquals(1, cmResponse.getStatusCode());
    }

    @Test
    public void canDeletePersistenceObjectsWithDescendantsWhenSearchResultsReturnsObjectsWithFdns() {
        setupManagedObjectMocks();
        mockSearchResult("FDN");
        final CmResponse cmResponse = objUnderTest.deletePersistenceObjectsWithDescendants(cmSearchCriteria);
        assertEquals("Dit not get expected status code and message was " + cmResponse.getStatusMessage(), 1, cmResponse.getStatusCode());
    }

    @Test
    public void deletePersistenceObjectsWithoutDescendantsReturnCorrectStatusOnCmValidationException() {
        final String errorMessage = "Mo has descendants";
        setupManagedObjectMocks();
        mockSearchResult("FDN");
        Mockito.doThrow(new CmValidationException(errorMessage)).when(validationService).ensureManagedObjectHasNoChildren(managedObject);
        final CmResponse cmResponse = objUnderTest.deletePersistenceObjects(cmSearchCriteria);
        assertEquals(-1, cmResponse.getStatusCode());
        assertEquals(errorMessage, cmResponse.getStatusMessage());
    }

    @Test
    public void canSetManagedObjectsAttributes() {
        setupManagedObjectMocks();
        final List<CmObject> cmObjects = createCmObjects();
        addAttributeSpecifications();
        cmObjectSpecification = createCmObjectSpecification(namespace, namespaceVersion, type, name, attributeSpecifications);
        final CmSearchCriteria cmSearchCriteria = createCmSearchCriteria();
        when(cmSearchEngine.search(cmSearchCriteria, null)).thenReturn(cmObjects);
        when(validationService.findManagedObject(cmObjects.iterator().next().getFdn())).thenReturn(managedObject);
        when(validationService.getTypeValidatedAttributes(namespace, namespaceVersion, type, attributeSpecifications, ValidationMode.WRITE_SET))
                .thenReturn(validatedAttributeSpecifications);
        final CmResponse cmResponse = objUnderTest.setManagedObjectsAttributes(cmSearchCriteria, attributeSpecifications);
        assertEquals(1, cmResponse.getStatusCode());
    }

    @Test
    public void setManagedObjectsReturnsMessageFromValidationServiceWhenValidationFails() {
        setupManagedObjectMocks();
        final String errorMessage = "0 instance(s) updated";
        final List<CmObject> cmObjects = createCmObjects();
        addAttributeSpecifications();
        cmObjectSpecification = createCmObjectSpecification(namespace, namespaceVersion, type, name, attributeSpecifications);
        final CmSearchCriteria cmSearchCriteria = createCmSearchCriteria();
        when(cmSearchEngine.search(cmSearchCriteria, null)).thenReturn(cmObjects);
        when(validationService.findManagedObject(anyString())).thenThrow(new CmValidationException(errorMessage));
        final CmResponse result = objUnderTest.setManagedObjectsAttributes(cmSearchCriteria, attributeSpecifications);
        assertEquals(errorMessage, result.getStatusMessage());
        assertEquals(-1, result.getStatusCode());
    }

    @Test
    public void setManagedObjectsReturnsExecutionErrorWhenValidationThrowsCmValidationException() {
        setupManagedObjectMocks();
        final List<CmObject> cmObjects = createCmObjects();
        cmObjectSpecification = createCmObjectSpecification(namespace, namespaceVersion, type, name, attributeSpecifications);
        final CmSearchCriteria cmSearchCriteria = createCmSearchCriteria();
        when(cmSearchEngine.search(cmSearchCriteria, null)).thenReturn(cmObjects);
        when(validationService.findManagedObject(anyString())).thenThrow(new RuntimeException(""));
        final CmResponse result = objUnderTest.setManagedObjectsAttributes(cmSearchCriteria, null);
        assertEquals(ErrorHandler.UNEXPECTED_ERROR, result.getStatusCode());
    }

    @Test
    public void canPerformActionByFdn() {
        setupManagedObjectMocks();
        final ActionSpecification actionSpecification = new ActionSpecification(actionName);
        final CmResponse result = objUnderTest.performAction(fdn, actionSpecification);
        assertEquals("1 instance(s)", result.getStatusMessage());
    }

    @Test
    public void canPerformActionBySearchCriteria() {
        setupManagedObjectMocks();
        final ActionSpecification actionSpecification = new ActionSpecification(actionName);

        final List<CmObject> cmObjects = createCmObjectsWithValues();
        final Long poid = cmObjects.get(0).getPoId();
        final CmSearchCriteria cmSearchCriteria = createCmSearchCriteria();
        when(cmSearchEngine.search(cmSearchCriteria, null)).thenReturn(cmObjects);
        when(liveBucket.findPoById(poid)).thenReturn(managedObject);
        final CmResponse result = objUnderTest.performAction(cmSearchCriteria, actionSpecification);
        assertEquals("1 instance(s)", result.getStatusMessage());
    }

    @Test
    public void performActionByFdnReturnsCorrectErrorMessageOnCmValidationException() {
        final String errorMessage = "FDN Validation Error";
        setupManagedObjectMocks();
        final ActionSpecification actionSpecification = new ActionSpecification(actionName);
        when(validationService.findManagedObject(anyString())).thenThrow(new CmValidationException(errorMessage));
        final CmResponse result = objUnderTest.performAction(fdn, actionSpecification);
        assertEquals(errorMessage, result.getStatusMessage());
    }

    @Test
    public void performActionByQueryReturnsCorrectErrorMessageOnCmValidationException() {
        final String errorMessage = "Search Validation Error";
        setupManagedObjectMocks();
        final ActionSpecification actionSpecification = new ActionSpecification(actionName);

        final List<CmObject> cmObjects = createCmObjectsWithValues();
        final Long poid = cmObjects.get(0).getPoId();
        final CmSearchCriteria cmSearchCriteria = createCmSearchCriteria();
        when(cmSearchEngine.search(cmSearchCriteria, null)).thenThrow(new CmValidationException(errorMessage));
        when(liveBucket.findPoById(poid)).thenReturn(managedObject);
        final CmResponse result = objUnderTest.performAction(cmSearchCriteria, actionSpecification);
        assertEquals(errorMessage, result.getStatusMessage());
    }

    @Test
    public void performActionByFdnReturnsCorrectErrorMessageOnRuntimeException() {

        final String errorMessage = "Some Exception";
        setupManagedObjectMocks();
        final ActionSpecification actionSpecification = new ActionSpecification(actionName);
        when(validationService.findManagedObject(anyString())).thenThrow(new RuntimeException(errorMessage));
        final CmResponse result = objUnderTest.performAction(fdn, actionSpecification);
        assertEquals(ErrorHandler.UNEXPECTED_ERROR, result.getStatusCode());
    }

    @Test
    public void performActionByQueryReturnsCorrectErrorMessageOnRuntimeException() {

        final String errorMessage = "Some Exception";
        setupManagedObjectMocks();
        final ActionSpecification actionSpecification = new ActionSpecification(actionName);

        final CmSearchCriteria cmSearchCriteria = createCmSearchCriteria();
        when(cmSearchEngine.search(cmSearchCriteria, null)).thenThrow(new RuntimeException(errorMessage));
        final CmResponse result = objUnderTest.performAction(cmSearchCriteria, actionSpecification);
        assertEquals(ErrorHandler.UNEXPECTED_ERROR, result.getStatusCode());
    }

    private List<CmObject> createCmObjects() {
        final CmObject newCmObject = new CmObject();
        final List<CmObject> cmObjects = new ArrayList<>();
        cmObjects.add(newCmObject);
        return cmObjects;
    }

    private List<CmObject> createCmObjectsWithValues() {
        final CmObject newCmObject = new CmObject();
        newCmObject.setNamespaceVersion("Some name space");
        newCmObject.setNamespaceVersion("1.0.1");
        newCmObject.setName("some name");
        newCmObject.setFdn("some fdn");
        newCmObject.setPoId(1l);
        final List<CmObject> cmObjects = new ArrayList<>();
        cmObjects.add(newCmObject);
        return cmObjects;
    }

    private CmSearchCriteria createCmSearchCriteria() {
        final CmSearchCriteria cmSearchCriteria = new CmSearchCriteria();
        cmSearchCriteria.setSingleCmObjectSpecification(cmObjectSpecification);
        final CmSearchScope scope = new CmSearchScope();
        scope.setScopeType(ScopeType.UNSPECIFIED);
        cmSearchCriteria.setSingleCmSearchScope(scope);
        return cmSearchCriteria;
    }

    private void addAttributeSpecifications() {
        final AttributeSpecification attributeSpecification = new AttributeSpecification();
        attributeSpecification.setName("userLabel");
        attributeSpecification.setValue("KAOS");
        attributeSpecifications.addAttributeSpecification(attributeSpecification);
    }

    private void setupMibRootMocks() {
        when(dataPersistenceService.getLiveBucket()).thenReturn(liveBucket);
        when(liveBucket.getMibRootBuilder()).thenReturn(mibRootBuilder);

        when(mibRootBuilder.namespace(anyString())).thenReturn(mibRootBuilder);
        when(mibRootBuilder.version(anyString())).thenReturn(mibRootBuilder);
        when(mibRootBuilder.type(anyString())).thenReturn(mibRootBuilder);
        when(mibRootBuilder.name(anyString())).thenReturn(mibRootBuilder);
        when(mibRootBuilder.addAttributes(any(Map.class))).thenReturn(mibRootBuilder);
        when(mibRootBuilder.create()).thenReturn(managedObject);
        when(dpsObjectMapper.mapToCmObject(managedObject)).thenReturn(cmObject);
        when(mibRootBuilder.parent(any(ManagedObject.class))).thenReturn(mibRootBuilder);

        when(managedObject.getAllAttributes()).thenReturn(moAttributes);
    }

    private void setupManagedObjectMocks() {
        when(dataPersistenceService.getLiveBucket()).thenReturn(liveBucket);
        when(validationService.findManagedObject(fdn)).thenReturn(managedObject);
        when(liveBucket.getManagedObjectBuilder()).thenReturn(managedObjectBuilder);
        when(managedObjectBuilder.type(anyString())).thenReturn(managedObjectBuilder);
        when(managedObjectBuilder.name(anyString())).thenReturn(managedObjectBuilder);
        when(managedObjectBuilder.addAttributes(any(Map.class))).thenReturn(managedObjectBuilder);
        when(managedObjectBuilder.create()).thenReturn(managedObject);
        when(dpsObjectMapper.mapToCmObject(managedObject)).thenReturn(cmObject);
        when(dpsObjectMapper.mapToCmObject(any(PersistenceObject.class), any(Collection.class))).thenReturn(cmObject);
        when(managedObjectBuilder.parent(any(ManagedObject.class))).thenReturn(managedObjectBuilder);

        when(managedObject.getAllAttributes()).thenReturn(moAttributes);

        when(managedObject.getNamespace()).thenReturn(namespace);
        when(managedObject.getType()).thenReturn(type);
        when(managedObject.getVersion()).thenReturn(namespaceVersion);
        when(managedObject.performAction(actionName, actionAttributes)).thenReturn(actionObject);
        when(liveBucket.deletePo(managedObject)).thenReturn(1);
        cmObjects.add(cmObject);
    }

    private void setupPersistenceObjectMocks() {
        when(dataPersistenceService.getLiveBucket()).thenReturn(liveBucket);
        when(liveBucket.getPersistenceObjectBuilder()).thenReturn(persistenceObjectBuilder);
        when(persistenceObjectBuilder.type(type)).thenReturn(persistenceObjectBuilder);
        when(persistenceObjectBuilder.version(namespaceVersion)).thenReturn(persistenceObjectBuilder);
        when(persistenceObjectBuilder.namespace(namespace)).thenReturn(persistenceObjectBuilder);
        when(persistenceObjectBuilder.addAttributes(validatedAttributes)).thenReturn(persistenceObjectBuilder);
        when(persistenceObjectBuilder.create()).thenReturn(persistenceObject);
        when(dpsObjectMapper.mapToCmObject(persistenceObject)).thenReturn(cmObject);
        when(persistenceObject.getAllAttributes()).thenReturn(validatedAttributes);
    }

    private CmObjectSpecification createCmObjectSpecification(final String namespace, final String namespaceVersion, final String type,
            final String name, final AttributeSpecifications attributeSpecifications) {
        final CmObjectSpecification cmObjectSpecification = new CmObjectSpecification();
        cmObjectSpecification.setNamespace(namespace);
        cmObjectSpecification.setNamespaceVersion(namespaceVersion);
        cmObjectSpecification.setType(type);
        cmObjectSpecification.setName(name);
        cmObjectSpecification.setAttributeSpecifications(attributeSpecifications);
        return cmObjectSpecification;
    }

    private void mockSearchResult(final Object id) {
        when(cmSearchEngine.search(cmSearchCriteria, null)).thenReturn(cmObjects);
        if (id instanceof String) {
            when(cmObject.getFdn()).thenReturn((String) id);
            when(validationService.findManagedObject((String) id)).thenReturn(managedObject);
        } else {
            when(cmObject.getPoId()).thenReturn((Long) (id));
            when(liveBucket.findPoById((Long) (id))).thenReturn(persistenceObject);
            when(liveBucket.deletePo(persistenceObject)).thenReturn(1);
        }
    }
}
