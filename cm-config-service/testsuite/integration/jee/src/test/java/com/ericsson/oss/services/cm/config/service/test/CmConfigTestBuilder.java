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
package com.ericsson.oss.services.cm.config.service.test;

import java.util.Iterator;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.slf4j.Logger;

import com.ericsson.oss.itpf.datalayer.dps.DataBucket;
import com.ericsson.oss.itpf.datalayer.dps.DataPersistenceService;
import com.ericsson.oss.itpf.datalayer.dps.exception.general.ObjectNotFoundException;
import com.ericsson.oss.itpf.datalayer.dps.persistence.ManagedObject;
import com.ericsson.oss.itpf.datalayer.dps.persistence.PersistenceObject;
import com.ericsson.oss.itpf.datalayer.dps.query.Query;
import com.ericsson.oss.itpf.datalayer.dps.query.QueryBuilder;
import com.ericsson.oss.services.cm.cmshared.dto.CmObject;
import com.ericsson.oss.services.cm.dto.mapping.DpsObjectMapper;
import com.ericsson.oss.services.cm.dto.mapping.DpsObjectMapperImpl;
import com.ericsson.oss.services.cm.testutil.DpsTestMoFactory;
import com.ericsson.oss.services.cm.testutil.DpsTestMoType;
import com.ericsson.oss.services.cm.testutil.DpsTestNode;
import com.ericsson.oss.services.cm.testutil.DpsTestNodeStub;
import com.ericsson.oss.services.cm.testutil.TestDataBuilder;
import com.ericsson.oss.services.cm.testutil.mo.DpsTestMo;

/**
 * A util class for CM Diff testing.
 */
@Default
public class CmConfigTestBuilder implements TestDataBuilder {
    protected DpsTestNode dpsTestNode1;
    protected DpsTestNode dpsTestNode2;
    protected DpsTestNode dpsTestNode3;

    @Inject
    protected Logger logger;

    @Inject
    protected UserTransaction userTransaction;

    DpsObjectMapper dpsManagedObjectMapper = new DpsObjectMapperImpl();

    DpsTestMoFactory dpsTestMoFactory = new DpsTestMoFactory();

    DataPersistenceService dataPersistenceService;

    public void setDataPersistenceService(final DataPersistenceService dataPersistenceService) {
        this.dataPersistenceService = dataPersistenceService;
    }

    /*
     * TOPOLOGY HANDLING.
     */

    @Override
    public void createTestData(final String configuration) throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException,
            HeuristicRollbackException {
        removeTestData(configuration);
        this.dpsTestNode1 = createNode(NODE_NAME1, configuration, TOP_NAMESPACE_VERSION, CPP_NAMESPACE_VERSION, ERBS_NAMESPACE_VERSION);
        this.dpsTestNode2 = createNode(NODE_NAME2, configuration, TOP_NAMESPACE_VERSION, CPP_NAMESPACE_VERSION, ERBS_NAMESPACE_VERSION);
        this.dpsTestNode3 = createNode(NODE_NAME3, configuration, TOP_NAMESPACE_VERSION, CPP_NAMESPACE_VERSION, ERBS_NAMESPACE_VERSION);
    }

    @Override
    public void removeTestData(final String configuration) throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException,
            HeuristicRollbackException {
        try {
            deleteAllNodes(configuration);
        } finally {
            rollbackTransactionIfActive();
        }

        try {
            deleteConfiguration(configuration);
        } finally {
            rollbackTransactionIfActive();
        }
    }

    protected DataBucket createConfiguration(final String configuration) {
        return this.dataPersistenceService.createDataBucket(configuration, configuration);
    }

    protected DpsTestNode createNode(final String userLabel) throws NotSupportedException, SystemException, RollbackException,
            HeuristicMixedException, HeuristicRollbackException {
        return createNode(userLabel, CONFIGURATION_FOR_TEST, TOP_NAMESPACE_VERSION, CPP_NAMESPACE_VERSION, ERBS_NAMESPACE_VERSION);
    }

    protected DpsTestNode createNode(final String userLabel, final String configuration, final String ossTopNamespaceVersion,
            final String cppNamespaceVersion, final String erbsNamespaceVersion) throws NotSupportedException, SystemException, RollbackException,
            HeuristicMixedException, HeuristicRollbackException {
        this.userTransaction.begin();
        final DataBucket bucket = tryCreateConfiguration(configuration);
        final ManagedObject meContextManagedObject = createRootTestMoInDps(bucket, DpsTestMoType.MECONTEXT, userLabel, ossTopNamespaceVersion);
        final CmObject meContextAsCmObject = this.dpsManagedObjectMapper.mapToCmObject(meContextManagedObject);
        final ManagedObject managedElementManagedObject = createTestMoInDps(bucket, DpsTestMoType.MANAGED_ELEMENT, meContextManagedObject,
                DEFAULT_MO_ID, userLabel, cppNamespaceVersion);
        final CmObject managedElementAsCmObject = this.dpsManagedObjectMapper.mapToCmObject(managedElementManagedObject);
        final ManagedObject eNodeBFunctionManagedObject = createTestMoInDps(bucket, DpsTestMoType.ENODEB_FUNCTION, managedElementManagedObject,
                DEFAULT_MO_ID, userLabel, erbsNamespaceVersion);
        final CmObject eNodeBFunctionAsCmObject = this.dpsManagedObjectMapper.mapToCmObject(eNodeBFunctionManagedObject);
        final ManagedObject sectorEquipmentFunctionManagedObject = createTestMoInDps(bucket, DpsTestMoType.SECTOR_EQUIPMENT_FUNCTION,
                managedElementManagedObject, DEFAULT_MO_ID, userLabel, erbsNamespaceVersion);
        final CmObject sectorEquipmentFunctionAsCmObject = this.dpsManagedObjectMapper.mapToCmObject(sectorEquipmentFunctionManagedObject);
        final ManagedObject eUtranCellFddManagedObject = createTestMoInDps(bucket, DpsTestMoType.EUTRAN_CELL_FDD, eNodeBFunctionManagedObject,
                EUCFDD1_ID, userLabel, erbsNamespaceVersion);
        final CmObject eUtranCellFDDAsCmObject = this.dpsManagedObjectMapper.mapToCmObject(eUtranCellFddManagedObject);
        this.userTransaction.commit();
        final DpsTestNode dpsTestNode = new DpsTestNode();
        dpsTestNode.setMeContextAsCmObject(meContextAsCmObject);
        dpsTestNode.setManagedElementAsCmObject(managedElementAsCmObject);
        dpsTestNode.setENodeBFunctionAsCmObject(eNodeBFunctionAsCmObject);
        dpsTestNode.setSectorEquipmentFunctionAsCmObject(sectorEquipmentFunctionAsCmObject);
        dpsTestNode.setEUtranCellFDDAsCmObject(eUtranCellFDDAsCmObject);
        return dpsTestNode;
    }

    protected DpsTestNodeStub createNodeStub(final String userLabel) throws NotSupportedException, SystemException, RollbackException,
            HeuristicMixedException, HeuristicRollbackException {
        return createNode(userLabel, CONFIGURATION_FOR_TEST, TOP_NAMESPACE_VERSION, CPP_NAMESPACE_VERSION, ERBS_NAMESPACE_VERSION);
    }

    // Use this method for tests which require the node MO tree to be created down to ManagedElement only; e.g. create ENodeBFunction
    protected DpsTestNodeStub createNodeStub(final String userLabel, final String configuration, final String version) throws NotSupportedException,
            SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
        this.userTransaction.begin();
        final DataBucket bucket = tryCreateConfiguration(configuration);
        final ManagedObject meContextManagedObject = createRootTestMoInDps(bucket, DpsTestMoType.MECONTEXT, userLabel, version);
        final CmObject meContextAsCmObject = this.dpsManagedObjectMapper.mapToCmObject(meContextManagedObject);
        final ManagedObject managedElementManagedObject = createTestMoInDps(bucket, DpsTestMoType.MANAGED_ELEMENT, meContextManagedObject,
                DEFAULT_MO_ID, userLabel, version);
        final CmObject managedElementAsCmObject = this.dpsManagedObjectMapper.mapToCmObject(managedElementManagedObject);
        this.userTransaction.commit();
        final DpsTestNodeStub dpsTestNodeStub = new DpsTestNodeStub();
        dpsTestNodeStub.setMeContextAsCmObject(meContextAsCmObject);
        dpsTestNodeStub.setManagedElementAsCmObject(managedElementAsCmObject);
        return dpsTestNodeStub;
    }

    /**
     * Gets the configuration or creates it if the configuration doesn't exist.
     *
     * @param configuration
     * @return
     */
    protected DataBucket tryCreateConfiguration(final String configuration) {
        try {
            final DataBucket dataBucket = this.dataPersistenceService.getDataBucket(configuration);
            return dataBucket;
        } catch (final ObjectNotFoundException objectNotFoundException) {
            return this.dataPersistenceService.createDataBucket(configuration, configuration);
        }
    }

    protected ManagedObject createRootTestMoInDps(final DataBucket dataBucket, final DpsTestMoType dpsTestMoType, final String moId,
            final String version) {
        final DpsTestMo dpsTestMo = this.dpsTestMoFactory.createTestMo(dpsTestMoType, version, moId, null);
        final ManagedObject managedObject = dataBucket.getMibRootBuilder().namespace(dpsTestMo.getNamespace()).type(dpsTestMo.getType())
                .version(dpsTestMo.getVersion()).name(dpsTestMo.getId()).addAttributes(dpsTestMo.getAttributes()).create();
        this.logger.debug("Test Setup, created root MO: " + managedObject.getFdn() + " in DPS");
        return managedObject;
    }

    protected ManagedObject createTestMoInDps(final DataBucket dataBucket, final DpsTestMoType dpsMoType, final ManagedObject parentManagedObject,
            final String moId, final String userLabel, final String version) {
        final DpsTestMo dpsTestMo = this.dpsTestMoFactory.createTestMo(dpsMoType, version, moId, userLabel);
        final ManagedObject managedObject = dataBucket.getMibRootBuilder().namespace(dpsTestMo.getNamespace()).type(dpsTestMo.getType())
                .version(dpsTestMo.getVersion()).name(dpsTestMo.getId()).addAttributes(dpsTestMo.getAttributes()).parent(parentManagedObject)
                .create();
        this.logger.debug("Test Setup, created MO: " + managedObject.getFdn() + " in DPS");
        return managedObject;
    }

    protected void deleteAllNodes(final String configuration) throws NotSupportedException, SystemException, RollbackException,
            HeuristicMixedException, HeuristicRollbackException {
        deleteAllNodes(TOP_NAMESPACE, "MeContext", configuration);
        /*
         * TODO enable deletes below cannot currently cant do this as there is a bug in DPS which doesn't let us delete 'orphaned' MOs see
         * http://jira-oss.lmera.ericsson.se/browse/TORF-4622#comment-254860
         */
        // deleteAllNodes(CPP_NAMESPACE, "ManagedElement");
        // deleteAllNodes(ERBS_NAMESPACE, "ENodeBFunction");
    }

    protected void deleteAllNodes(final String namespace, final String type, final String configuration) throws NotSupportedException,
            SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
        this.userTransaction.begin();
        final QueryBuilder queryBuilder = this.dataPersistenceService.getQueryBuilder();
        final Query query = queryBuilder.createTypeQuery(namespace, type);
        final Iterator<PersistenceObject> iterator = tryCreateConfiguration(configuration).getQueryExecutor().execute(query);
        while (iterator.hasNext()) {
            final PersistenceObject persistenceObject = iterator.next();
            tryCreateConfiguration(configuration).deletePo(persistenceObject);
        }
        this.userTransaction.commit();
    }

    /**
     * Deletes the configuration if it is not the Live configuration.
     *
     * @param configuration
     *            configuration name
     * @throws SystemException
     * @throws NotSupportedException
     * @throws HeuristicRollbackException
     * @throws HeuristicMixedException
     * @throws RollbackException
     */
    protected void deleteConfiguration(final String configuration) throws NotSupportedException, SystemException, RollbackException,
            HeuristicMixedException, HeuristicRollbackException {
        if (!configuration.equals("Live")) {
            this.userTransaction.begin();
            this.dataPersistenceService.deleteDataBucket(configuration, true);
            this.userTransaction.commit();
        }
    }

    /**
     * TRANSACTION - HANDLING.
     */
    private void rollbackTransactionIfActive() throws SystemException {
        if (isActive(this.userTransaction) || isMarkedForRollback(this.userTransaction)) {
            this.userTransaction.rollback();
        }
    }

    private boolean isMarkedForRollback(final UserTransaction userTransaction) {
        try {
            final int status = userTransaction.getStatus();
            return status == Status.STATUS_MARKED_ROLLBACK;
        } catch (final SystemException e) {
            return false;
        }
    }

    private boolean isActive(final UserTransaction userTransaction) {
        try {
            final int status = userTransaction.getStatus();
            return status == Status.STATUS_ACTIVE;
        } catch (final SystemException e) {
            return false;
        }
    }
}
