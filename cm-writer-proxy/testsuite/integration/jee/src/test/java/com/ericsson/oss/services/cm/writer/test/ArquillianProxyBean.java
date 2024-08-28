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
package com.ericsson.oss.services.cm.writer.test;

import java.util.Map;

import javax.ejb.Stateless;

import com.ericsson.oss.itpf.sdk.core.annotation.EServiceRef;
import com.ericsson.oss.services.cm.cmshared.dto.ActionSpecification;
import com.ericsson.oss.services.cm.cmshared.dto.AttributeSpecifications;
import com.ericsson.oss.services.cm.cmshared.dto.CmObjectSpecification;
import com.ericsson.oss.services.cm.cmshared.dto.CmResponse;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchCriteria;
import com.ericsson.oss.services.cm.cmwriter.api.CmWriterServiceProxy;

@Stateless
public class ArquillianProxyBean implements ArquillianProxy {

    @EServiceRef
    protected CmWriterServiceProxy cmWriter;

    @Override
    public CmResponse createNodeRootMib(final CmObjectSpecification cmObjectSpecification) {
        return cmWriter.createNodeRootMib(cmObjectSpecification);
    }

    @Override
    public CmResponse createMibRoot(final String parentFdn, final CmObjectSpecification cmObjectSpecification) {
        return cmWriter.createMibRoot(parentFdn, cmObjectSpecification);
    }

    @Override
    public CmResponse createManagedObject(final String parentFdn, final CmObjectSpecification cmObjectSpecification) {
        return cmWriter.createManagedObject(parentFdn, cmObjectSpecification);
    }

    @Override
    public CmResponse createPersistenceObject(final String namespace, final String type, final String version,
                                              final Map<String, Object> validatedAttributes) {
        // TODO Add Arquillian tests
        return null;
    }

    @Override
    public CmResponse setManagedObjectAttributes(final String fdn, final AttributeSpecifications attributes) {
        return cmWriter.setManagedObjectAttributes(fdn, attributes);
    }

    @Override
    public CmResponse setManagedObjectsAttributes(final CmSearchCriteria cmSearchCriteria, final AttributeSpecifications attributes) {
        return cmWriter.setManagedObjectsAttributes(cmSearchCriteria, attributes);
    }

    @Override
    public CmResponse performAction(final String targetFdn, final ActionSpecification actionSpecification) {
        return cmWriter.performAction(targetFdn, actionSpecification);
    }

    @Override
    public CmResponse performAction(final CmSearchCriteria cmSearchCriteria, final ActionSpecification actionSpecification) {
        return cmWriter.performAction(cmSearchCriteria, actionSpecification);
    }

    @Override
    public CmResponse deleteManagedObject(final String fdn) {
        return cmWriter.deleteManagedObject(fdn);
    }

    @Override
    public CmResponse deleteManagedObjectWithDescendants(final String fdn) {
        return cmWriter.deleteManagedObjectWithDescendants(fdn);
    }

    @Override
    public CmResponse deletePersistenceObjects(final CmSearchCriteria cmSearchCriteria) {
        return cmWriter.deletePersistenceObjects(cmSearchCriteria);
    }

    @Override
    public CmResponse deletePersistenceObjectsWithDescendants(final CmSearchCriteria cmSearchCriteria) {
        return cmWriter.deletePersistenceObjectsWithDescendants(cmSearchCriteria);
    }

	/* (non-Javadoc)
	 * @see com.ericsson.oss.services.cm.cmwriter.api.CmWriterServiceProxy#createNodeRootMib(com.ericsson.oss.services.cm.cmshared.dto.CmObjectSpecification, java.lang.String)
	 */
	@Override
	public CmResponse createNodeRootMib(
			CmObjectSpecification cmObjectSpecification, String bucketName) {
		return cmWriter.createNodeRootMib(cmObjectSpecification, bucketName);
	}

	/* (non-Javadoc)
	 * @see com.ericsson.oss.services.cm.cmwriter.api.CmWriterServiceProxy#createMibRoot(java.lang.String, com.ericsson.oss.services.cm.cmshared.dto.CmObjectSpecification, java.lang.String)
	 */
	@Override
	public CmResponse createMibRoot(String parentFdn,
			CmObjectSpecification cmObjectSpecification, String bucketName) {
		return cmWriter.createMibRoot(parentFdn, cmObjectSpecification, bucketName);
	}

	/* (non-Javadoc)
	 * @see com.ericsson.oss.services.cm.cmwriter.api.CmWriterServiceProxy#createManagedObject(java.lang.String, com.ericsson.oss.services.cm.cmshared.dto.CmObjectSpecification, java.lang.String)
	 */
	@Override
	public CmResponse createManagedObject(String parentFdn,
			CmObjectSpecification cmObjectSpecification, String bucketName) {
		return cmWriter.createManagedObject(parentFdn, cmObjectSpecification, bucketName);
	}

	/* (non-Javadoc)
	 * @see com.ericsson.oss.services.cm.cmwriter.api.CmWriterServiceProxy#createPersistenceObject(java.lang.String, java.lang.String, java.lang.String, java.util.Map, java.lang.String)
	 */
	@Override
	public CmResponse createPersistenceObject(String namespace, String type,
			String version, Map<String, Object> validatedAttributes,
			String bucketName) {
		return cmWriter.createPersistenceObject(namespace, type, version, validatedAttributes, bucketName);
	}

	/* (non-Javadoc)
	 * @see com.ericsson.oss.services.cm.cmwriter.api.CmWriterServiceProxy#setManagedObjectAttributes(java.lang.String, com.ericsson.oss.services.cm.cmshared.dto.AttributeSpecifications, java.lang.String)
	 */
	@Override
	public CmResponse setManagedObjectAttributes(String fdn,
			AttributeSpecifications attributeSpecifications, String bucketName) {

		return cmWriter.setManagedObjectAttributes(fdn, attributeSpecifications, bucketName);
	}

	/* (non-Javadoc)
	 * @see com.ericsson.oss.services.cm.cmwriter.api.CmWriterServiceProxy#setManagedObjectsAttributes(com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchCriteria, com.ericsson.oss.services.cm.cmshared.dto.AttributeSpecifications, java.lang.String)
	 */
	@Override
	public CmResponse setManagedObjectsAttributes(
			CmSearchCriteria cmSearchCriteria,
			AttributeSpecifications attributes, String bucketName) {
		return cmWriter.setManagedObjectsAttributes(cmSearchCriteria, attributes, bucketName);
	}

	/* (non-Javadoc)
	 * @see com.ericsson.oss.services.cm.cmwriter.api.CmWriterServiceProxy#performAction(java.lang.String, com.ericsson.oss.services.cm.cmshared.dto.ActionSpecification, java.lang.String)
	 */
	@Override
	public CmResponse performAction(String targetFdn,
			ActionSpecification actionSpecification, String bucketName) {
		return cmWriter.performAction(targetFdn, actionSpecification, bucketName);
	}

	/* (non-Javadoc)
	 * @see com.ericsson.oss.services.cm.cmwriter.api.CmWriterServiceProxy#performAction(com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchCriteria, com.ericsson.oss.services.cm.cmshared.dto.ActionSpecification, java.lang.String)
	 */
	@Override
	public CmResponse performAction(CmSearchCriteria cmSearchCriteria,
			ActionSpecification actionSpecification, String bucketName) {

		return cmWriter.performAction(cmSearchCriteria, actionSpecification, bucketName);
	}

	/* (non-Javadoc)
	 * @see com.ericsson.oss.services.cm.cmwriter.api.CmWriterServiceProxy#deleteManagedObject(java.lang.String, java.lang.String)
	 */
	@Override
	public CmResponse deleteManagedObject(String fdn, String bucketName) {

		return cmWriter.deleteManagedObject(fdn, bucketName);
	}

	/* (non-Javadoc)
	 * @see com.ericsson.oss.services.cm.cmwriter.api.CmWriterServiceProxy#deleteManagedObjectWithDescendants(java.lang.String, java.lang.String)
	 */
	@Override
	public CmResponse deleteManagedObjectWithDescendants(String fdn,
			String bucketName) {
		return cmWriter.deleteManagedObject(fdn, bucketName);
	}

	/* (non-Javadoc)
	 * @see com.ericsson.oss.services.cm.cmwriter.api.CmWriterServiceProxy#deletePersistenceObjects(com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchCriteria, java.lang.String)
	 */
	@Override
	public CmResponse deletePersistenceObjects(
			CmSearchCriteria cmSearchCriteria, String bucketName) {

		return cmWriter.deletePersistenceObjects(cmSearchCriteria, bucketName);
	}

	/* (non-Javadoc)
	 * @see com.ericsson.oss.services.cm.cmwriter.api.CmWriterServiceProxy#deletePersistenceObjectsWithDescendants(com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchCriteria, java.lang.String)
	 */
	@Override
	public CmResponse deletePersistenceObjectsWithDescendants(
			CmSearchCriteria cmSearchCriteria, String bucketName) {
 
		return cmWriter.deletePersistenceObjectsWithDescendants(cmSearchCriteria, bucketName);
	}

}
