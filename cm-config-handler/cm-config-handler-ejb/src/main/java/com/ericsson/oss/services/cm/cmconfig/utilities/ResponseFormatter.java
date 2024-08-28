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
package com.ericsson.oss.services.cm.cmconfig.utilities;

import static com.ericsson.oss.services.cm.cmconfig.utilities.CmConfigConstants.FDN;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.Default;

import com.ericsson.oss.services.cm.cmshared.dto.CmObject;
import com.ericsson.oss.services.cm.cmshared.dto.CmResponse;
import com.ericsson.oss.services.scriptengine.spi.dtos.AbstractDto;
import com.ericsson.oss.services.scriptengine.spi.dtos.CommandResponseDto;
import com.ericsson.oss.services.scriptengine.spi.dtos.ListDto;
import com.ericsson.oss.services.scriptengine.spi.dtos.NameValueDto;
import com.ericsson.oss.services.scriptengine.spi.dtos.OrderedTableDto;

/**
 * Contains methods to mapping a {@link CmResponse} object returned by
 * cm-config-service to a {@link CommandResponseDto}.
 * 
 * 
 */
@Default
public class ResponseFormatter {

    private CommandResponseDto generateCommandResponseMetaData(final CmResponse cmResponse) {
        final CommandResponseDto editorResponseDto = new CommandResponseDto();
        editorResponseDto.setStatusCode(cmResponse.getStatusCode());
        editorResponseDto.setStatusMessage(cmResponse.getStatusMessage());
        return editorResponseDto;
    }

    /**
     * Generate a {@link CommandResponseDto} with a List format.
     * 
     * @param cmResponse
     *            cmresponse to map.
     * @return CommandResponseDTO
     */
    public CommandResponseDto generateCommandResponseDtoListFormat(final CmResponse cmResponse) {
        final CommandResponseDto commandResponseDto = this.generateCommandResponseMetaData(cmResponse);
        commandResponseDto.setCommandResultDto(createCommandResultList(cmResponse.getCmObjects()));
        return commandResponseDto;
    }

    /**
     * Generate a {@link CommandResponseDto} with a Table format.
     * 
     * @param cmResponse
     *            cmresponse to map.
     * @return CommandResponseDTO
     */
    public CommandResponseDto generateCommandResponseDtoTableFormat(final CmResponse cmResponse) {
        final CommandResponseDto commandResponseDto = this.generateCommandResponseMetaData(cmResponse);
        commandResponseDto.setCommandResultDto(createCommandResultTable(cmResponse.getCmObjects()));
        return commandResponseDto;
    }

    private AbstractDto createCommandResultList(final Collection<CmObject> cmObjects) {
        final ListDto result = new ListDto();
        result.setDtoName("ManagedObject Result List");
        for (final CmObject cmObject : cmObjects) {
            result.addDto(createManagedObjectDto(cmObject));
        }
        return result;
    }

    private AbstractDto createCommandResultTable(final Collection<CmObject> cmObjects) {
        final ListDto result = new ListDto();
        result.setDtoName("Table Result");
        final Map<String, List<CmObject>> cmObjectGroupedByManagedObjectType = new HashMap<>();

        for (final CmObject cmObject : cmObjects) {
            addCmObjectToCorrectList(cmObjectGroupedByManagedObjectType, cmObject);
        }
        populateTables(result, cmObjectGroupedByManagedObjectType);

        return result;
    }

    private void populateTables(final ListDto result, final Map<String, List<CmObject>> columnRowMap) {
        for (final String type : columnRowMap.keySet()) {
            final OrderedTableDto<Integer, String, String> tableDto = new OrderedTableDto<>();
            tableDto.setDtoName(type);
            int rowId = 0;
            for (final CmObject cmObject : columnRowMap.get(type)) {
                tableDto.addCell(rowId, "FDN", cmObject.getFdn());
                final Map<String, Object> attributes = cmObject.getAttributes();
                for (final String attributeName : attributes.keySet()) {
                    tableDto.addCell(rowId, attributeName, moAttributeValueToString(attributes.get(attributeName)));
                }
                ++rowId;
            }
            result.addDto(tableDto);
        }
    }

    private void addCmObjectToCorrectList(final Map<String, List<CmObject>> cmObjectGroupedByManagedObjectType, final CmObject cmObject) {
        List<CmObject> cmObjectsForManagedObjectType = cmObjectGroupedByManagedObjectType.get(cmObject.getType());
        if (cmObjectsForManagedObjectType == null) {
            cmObjectsForManagedObjectType = new ArrayList<>();
            cmObjectGroupedByManagedObjectType.put(cmObject.getType(), cmObjectsForManagedObjectType);
        }
        cmObjectsForManagedObjectType.add(cmObject);
    }

    private AbstractDto createManagedObjectDto(final CmObject cmObject) {
        final ListDto managedObjectAsDto = getMoMetaData(cmObject);
        managedObjectAsDto.setDtoName(cmObject.getFdn());
        final ListDto attributesAsDto = getAttributesFromOneMo(cmObject);
        for (final AbstractDto attribute : attributesAsDto.getDtos()) {
            managedObjectAsDto.addDto(attribute);
        }
        return managedObjectAsDto;
    }

    private ListDto getAttributesFromOneMo(final CmObject mo) {
        final ListDto attributesDto = new ListDto();
        attributesDto.setDtoName("attributes");
        for (final Map.Entry<String, Object> attribute : mo.getAttributes().entrySet()) {
            final NameValueDto attributeDto = new NameValueDto();
            attributeDto.setName(attribute.getKey());
            attributeDto.setValue(moAttributeValueToString(attribute.getValue()));
            attributesDto.addDto(attributeDto);
        }
        return attributesDto;
    }

    private String moAttributeValueToString(final Object attributeValue) {
        // Please note that DPS can return null value
        if (attributeValue == null) {
            return "null";
        }
        return attributeValue.toString();
    }

    /**
     * @param mo
     * @return
     */
    private ListDto getMoMetaData(final CmObject mo) {
        final ListDto editorListDTO = new ListDto();
        if (mo.getFdn() != null) {
            /*
             * currently, describe command is using cmObjects but not using the
             * fdn field (i.e. fdn is null for describe command) but we don't
             * want to send back null fdns to the end-user(e.g. CLI app)
             */
            editorListDTO.addDto(createNameValueDto(FDN, mo.getFdn()));
        }
        return editorListDTO;
    }

    private static NameValueDto createNameValueDto(final String name, final String value) {
        final NameValueDto nameValueDto = new NameValueDto();
        nameValueDto.setName(name);
        nameValueDto.setValue(value);
        return nameValueDto;
    }

}
