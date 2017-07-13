/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.wcms.service;

import java.util.List;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.wcms.model.PropertyTypePipeSize;
import org.egov.wcms.repository.PropertyPipeSizeRepository;
import org.egov.wcms.web.contract.PropertyTypePipeSizeGetRequest;
import org.egov.wcms.web.contract.PropertyTypePipeSizeRequest;
import org.egov.wcms.web.contract.PropertyTypeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PropertyTypePipeSizeService {

    @Autowired
    private PropertyPipeSizeRepository propertyPipeSizeRepository;

    @Autowired
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private RestExternalMasterService restExternalMasterService;

    public PropertyTypePipeSizeRequest create(final PropertyTypePipeSizeRequest propertyPipeSizeRequest) {
        return propertyPipeSizeRepository.persistCreatePropertyPipeSize(propertyPipeSizeRequest);
    }

    public PropertyTypePipeSizeRequest update(final PropertyTypePipeSizeRequest propertyPipeSizeRequest) {
        return propertyPipeSizeRepository.persistUpdatePropertyPipeSize(propertyPipeSizeRequest);
    }

    public PropertyTypePipeSize createPropertyPipeSize(final String topic, final String key,
            final PropertyTypePipeSizeRequest propertyPipeSizeRequest) {

        try {
            kafkaTemplate.send(topic, key, propertyPipeSizeRequest);
        } catch (final Exception ex) {
            log.error("Exception Encountered : " + ex);
        }
        return propertyPipeSizeRequest.getPropertyTypePipeSize();
    }

    public boolean checkPropertyByPipeSize(final PropertyTypePipeSizeRequest propertyPipeSizeRequest) {
        getPropertyTypeByName(propertyPipeSizeRequest);
        return propertyPipeSizeRepository.checkPropertyByPipeSize(propertyPipeSizeRequest.getPropertyTypePipeSize().getId(),
                propertyPipeSizeRequest.getPropertyTypePipeSize().getPropertyTypeId(),
                propertyPipeSizeRequest.getPropertyTypePipeSize().getPipeSize(),
                propertyPipeSizeRequest.getPropertyTypePipeSize().getTenantId());
    }

    public List<PropertyTypePipeSize> getPropertyPipeSizes(
            final PropertyTypePipeSizeGetRequest propertyPipeSizeGetRequest) {
        if (propertyPipeSizeGetRequest.getPropertyTypeName() != null) {
            final PropertyTypeResponse propertyTypes = restExternalMasterService.getPropertyIdFromPTModule(
                    propertyPipeSizeGetRequest.getPropertyTypeName(), propertyPipeSizeGetRequest.getTenantId());
            if (propertyTypes != null)
                propertyPipeSizeGetRequest.setPropertyTypeId(propertyTypes.getPropertyTypes().get(0).getId());

        }
        return propertyPipeSizeRepository.findForCriteria(propertyPipeSizeGetRequest);

    }

    public Boolean getPropertyTypeByName(final PropertyTypePipeSizeRequest propertyPipeSizeRequest) {
        Boolean isValidProperty = Boolean.FALSE;
        final PropertyTypeResponse propertyTypes = restExternalMasterService.getPropertyIdFromPTModule(
                propertyPipeSizeRequest.getPropertyTypePipeSize().getPropertyTypeName(),
                propertyPipeSizeRequest.getPropertyTypePipeSize().getTenantId());

        if (propertyTypes.getPropertyTypesSize()) {
            isValidProperty = Boolean.TRUE;
            propertyPipeSizeRequest.getPropertyTypePipeSize().setPropertyTypeId(
                    propertyTypes.getPropertyTypes() != null && propertyTypes.getPropertyTypes().get(0) != null
                            ? propertyTypes.getPropertyTypes().get(0).getId() : "");

        }
        return isValidProperty;

    }

    public boolean checkPipeSizeExists(final Double pipeSize, final String tenantId) {
        return propertyPipeSizeRepository.checkPipeSizeExists(pipeSize, tenantId);
    }

}
