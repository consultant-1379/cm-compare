package com.ericsson.oss.services.cm.cmwriter.api;

import java.util.Map;

import javax.ejb.Remote;

import com.ericsson.oss.itpf.sdk.core.annotation.EService;
import com.ericsson.oss.services.cm.cmshared.dto.ActionSpecification;
import com.ericsson.oss.services.cm.cmshared.dto.AttributeSpecifications;
import com.ericsson.oss.services.cm.cmshared.dto.CmObject;
import com.ericsson.oss.services.cm.cmshared.dto.CmObjectSpecification;
import com.ericsson.oss.services.cm.cmshared.dto.CmResponse;
import com.ericsson.oss.services.cm.cmshared.dto.StringifiedAttributeSpecifications;
import com.ericsson.oss.services.cm.cmshared.dto.ValidatedAttributeSpecifications;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchCriteria;

/**
 * The Interface CmWriterService.
 */
@EService
@Remote
public interface CmWriterServiceProxy {

    /**
     * Create top MibRoot ManagedObject for a node (MeContext)
     * 
     * @param CmObjectSpecification
     *            the cmObjectSpecification for the object to create<br>
     *            The namespace has to be set to "OSS_TOP" and the type has to be "MeContext"<br>
     *            namespace version as to be set to he correct version of the deployed OSS_TOP model(s) e.g. "1.0.1"<br>
     *            The minimum attributeSpeciication will depend on the data in the version of the model<br>
     * @return CmResponse<br>
     *         Use {@link CmResponse#getStatusCode()} to see if the request was executed successfully<br>
     *         If successful {@link CmResponse#getCmObjects()} will contain 1 {@link CmObject} with all it's attributes
     */
    CmResponse createNodeRootMib(CmObjectSpecification cmObjectSpecification);
    
    CmResponse createNodeRootMib(CmObjectSpecification cmObjectSpecification, final String bucketName);

    /**
     * Create a MibRoot Managed Object (under an existing parent as specified by parentFdn)
     * 
     * @param parentFdn
     *            the Full Distinguished Name (FDN) of the parent under which to create the MibRoot managed object
     * @param CmObjectSpecification
     *            the cmObjectSpecification for the object to create<br>
     *            The namespace and namespace version have to be set to existing(deployed) models<br>
     *            The minimum attributeSpeciication will depend on the data in the version of the model<br>
     * @return CmResponse<br>
     *         Use {@link CmResponse#getStatusCode()} to see if the request was executed successfully<br>
     *         If successful {@link CmResponse#getCmObjects()} will contain 1 {@link CmObject} with all it's attributes
     */
    CmResponse createMibRoot(String parentFdn, CmObjectSpecification cmObjectSpecification);
    
    CmResponse createMibRoot(String parentFdn, CmObjectSpecification cmObjectSpecification, final String bucketName);

    /**
     * Create a ManagedObject Managed Object (under an existing parent as specified by parentFdn)
     * 
     * @param parentFdn
     *            the Full Distinguished Name (FDN) of the parent under which to create the managed object
     * @param CmObjectSpecification
     *            the cmObjectSpecification for the object to create<br>
     *            Name and type are compulsory but namespace and namespace version will be ignored e.g. can be left null. The minimum
     *            attributeSpeciication will depend on the data in the version of the model<br>
     * @return CmResponse<br>
     *         Use {@link CmResponse#getStatusCode()} to see if the request was executed successfully<br>
     *         If successful {@link CmResponse#getCmObjects()} will contain 1 {@link CmObject} with all it's attributes
     */

    CmResponse createManagedObject(String parentFdn, CmObjectSpecification cmObjectSpecification);
    
    CmResponse createManagedObject(String parentFdn, CmObjectSpecification cmObjectSpecification, final String bucketName);

    /**
     * This method needs to be updated. Correct API to follow
     * 
     * @param namespace
     * @param type
     * @param version
     * @param validatedAttributes
     *            The calling service assumes responsibility for the validity of the passed attributes.
     * @return (CmResponse.statusCode : <0 : error, >=0 : success)
     */
    CmResponse createPersistenceObject(final String namespace, String type, final String version, Map<String, Object> validatedAttributes);
    
    CmResponse createPersistenceObject(final String namespace, String type, final String version, Map<String, Object> validatedAttributes, final String bucketName);

    /**
     * @param fdn
     *            the Full Distinguished Name (FDN) of the object to modify
     * @param attributeSpecifications
     *            the attributeSpecifications for the attributes to be modified. This can be a {@link StringifiedAttributeSpecifications} or a
     *            {@link ValidatedAttributeSpecifications}.
     * @return CmResponse<br>
     *         Use {@link CmResponse#getStatusCode()} to see if the request was executed successfully<br>
     *         If successful {@link CmResponse#getCmObjects()} will contain 1 {@link CmObject} with the just the modified attributes
     */
    CmResponse setManagedObjectAttributes(String fdn, AttributeSpecifications attributeSpecifications);
    
    CmResponse setManagedObjectAttributes(String fdn, AttributeSpecifications attributeSpecifications, final String bucketName);

    /**
     * @param cmSearchCriteria
     *            The search criteria which will identify the target PersistenceObjects
     * @param attributes
     *            the attributeSpecifications for the attributes to be modified. This can be a {@link StringifiedAttributeSpecifications} or a
     *            {@link ValidatedAttributeSpecifications}.
     * @return CmResponse<br>
     *         Use {@link CmResponse#getStatusCode()} to see if the request was executed successfully<br>
     *         If successful {@link CmResponse#getCmObjects()} will contain 1 {@link CmObject} with the just the modified attributes
     */
    CmResponse setManagedObjectsAttributes(CmSearchCriteria cmSearchCriteria, AttributeSpecifications attributes);
    
    CmResponse setManagedObjectsAttributes(CmSearchCriteria cmSearchCriteria, AttributeSpecifications attributes, final String bucketName);
    /**
     * 
     * @param targetFdn
     *            The Full Distinguished Name (FDN) of the object which will perform the action
     * 
     * @param actionSpecification
     *            The action which will be validated and performed
     * @return CmResponse<br>
     *         Use {@link CmResponse#getStatusCode()} to show the number of ManagedObjects which have performed the action <br>
     *         If successful {@link CmResponse#getCmObjects()} will contain 1 {@link CmObject}
     */
    CmResponse performAction(final String targetFdn, final ActionSpecification actionSpecification);
    
    CmResponse performAction(final String targetFdn, final ActionSpecification actionSpecification, final String bucketName);

    /**
     * 
     * @param cmSearchCriteria
     *            The search criteria which will identify the target PersistenceObjects
     * @param actionSpecification
     *            The action which will be validated and performed
     * @return CmResponse<br>
     *         Use {@link CmResponse#getStatusCode()} to show the number of PersistenceObjects which have performed the action <br>
     *         executed successfully<br>
     *         If successful {@link CmResponse#getCmObjects()} will contain 1 to N {@link CmObject}
     */
    CmResponse performAction(final CmSearchCriteria cmSearchCriteria, final ActionSpecification actionSpecification);
    
    CmResponse performAction(final CmSearchCriteria cmSearchCriteria, final ActionSpecification actionSpecification, final String bucketName);
    /**
     * @param fdn
     *            the Full Distinguished Name (FDN) of the object to be deleted
     * @return CmResponse<br>
     *         Use {@link CmResponse#getStatusCode()} to see if the request was executed successfully<br>
     *         If successful {@link CmResponse#getCmObjects()} will contain 1 {@link CmObject}
     */
    CmResponse deleteManagedObject(final String fdn);
    
    CmResponse deleteManagedObject(final String fdn, final String bucketName);

    /**
     * @param fdn
     *            the Full Distinguished Name (FDN) of the object to be deleted
     * @return CmResponse<br>
     *         Use {@link CmResponse#getStatusCode()} to see if the request was executed successfully<br>
     *         If successful {@link CmResponse#getCmObjects()} will contain 1 to N{@link CmObject}
     */
    CmResponse deleteManagedObjectWithDescendants(final String fdn);
    
    CmResponse deleteManagedObjectWithDescendants(final String fdn, final String bucketName);

    /**
     * @param cmSearchCriteria
     *            The search criteria which will identify the target PersistenceObjects
     * @return CmResponse<br>
     *         Use {@link CmResponse#getStatusCode()} to see if the request was executed successfully<br>
     *         If successful {@link CmResponse#getCmObjects()} will contain 1 to N {@link CmObject}
     */
    CmResponse deletePersistenceObjects(CmSearchCriteria cmSearchCriteria);
    
    CmResponse deletePersistenceObjects(CmSearchCriteria cmSearchCriteria, final String bucketName);

    /**
     * @param cmSearchCriteria
     *            The search criteria which will identify the target PersistenceObjects
     * @return CmResponse<br>
     *         Use {@link CmResponse#getStatusCode()} to see if the request was executed successfully<br>
     *         If successful {@link CmResponse#getCmObjects()} will contain 1 to N {@link CmObject}
     */
    CmResponse deletePersistenceObjectsWithDescendants(CmSearchCriteria cmSearchCriteria);
    
    CmResponse deletePersistenceObjectsWithDescendants(CmSearchCriteria cmSearchCriteria, final String bucketName);
}