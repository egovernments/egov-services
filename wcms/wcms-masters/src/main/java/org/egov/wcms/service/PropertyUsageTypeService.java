/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.wcms.service;

import java.util.List;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.wcms.model.PropertyTypeUsageType;
import org.egov.wcms.repository.PropertyUsageTypeRepository;
import org.egov.wcms.util.WcmsConstants;
import org.egov.wcms.web.contract.PropertyTypeResponse;
import org.egov.wcms.web.contract.PropertyTypeUsageTypeGetReq;
import org.egov.wcms.web.contract.PropertyTypeUsageTypeReq;
import org.egov.wcms.web.contract.UsageTypeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PropertyUsageTypeService {

    @Autowired
    private PropertyUsageTypeRepository propUsageTypeRepository;

    @Autowired
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private RestWaterExternalMasterService restExternalMasterService;

    @Autowired
    private CodeGeneratorService codeGeneratorService;

    public PropertyTypeUsageTypeReq create(final PropertyTypeUsageTypeReq propUsageTypeRequest) {
        return propUsageTypeRepository.persistCreateUsageType(propUsageTypeRequest);
    }

    public PropertyTypeUsageTypeReq update(final PropertyTypeUsageTypeReq propUsageTypeRequest) {
        return propUsageTypeRepository.persistUpdateUsageType(propUsageTypeRequest);
    }

    public List<PropertyTypeUsageType> createPropertyUsageType(final String topic, final String key,
            final PropertyTypeUsageTypeReq propUsageTypeRequest) {
        for (final PropertyTypeUsageType propertyUsage : propUsageTypeRequest.getPropertyTypeUsageType()) {
            propertyUsage.setCode(codeGeneratorService.generate(PropertyTypeUsageType.SEQ_PROPERTYUSAGETYPE));
        }

        try {
            kafkaTemplate.send(topic, key, propUsageTypeRequest);
        } catch (final Exception ex) {
            log.error("Exception Encountered : " + ex);
        }
        return propUsageTypeRequest.getPropertyTypeUsageType();
    }

    public List<PropertyTypeUsageType> updatePropertyUsageType(final String topic, final String key,
            final PropertyTypeUsageTypeReq propUsageTypeRequest) {

        try {
            kafkaTemplate.send(topic, key, propUsageTypeRequest);
        } catch (final Exception ex) {
            log.error("Exception Encountered : " + ex);
        }
        return propUsageTypeRequest.getPropertyTypeUsageType();
    }

    public List<PropertyTypeUsageType> getPropertyUsageTypes(
            final PropertyTypeUsageTypeGetReq propUsageTypeGetRequest) {
        if (propUsageTypeGetRequest.getPropertyType() != null) {
            final PropertyTypeResponse propertyType = restExternalMasterService.getPropertyIdFromPTModule(
                    propUsageTypeGetRequest.getPropertyType(), propUsageTypeGetRequest.getTenantId());
            if (propertyType.getPropertyTypesSize())
                propUsageTypeGetRequest.setPropertyTypeId(propertyType.getPropertyTypes().get(0).getId());

        }
        //TODO: changing as per new requirement in Connection need to enhance API
        if (propUsageTypeGetRequest.getUsageCode() != null) {
            final UsageTypeResponse usageType = restExternalMasterService.getUsageIdFromPTModuleByCode(
                    propUsageTypeGetRequest.getUsageCode(),WcmsConstants.WC,
                    propUsageTypeGetRequest.getTenantId());
            if (usageType.getUsageTypesSize())
                propUsageTypeGetRequest.setUsageTypeId(usageType.getUsageMasters().get(0).getId());

        }
        return propUsageTypeRepository.getPropertyUsageType(propUsageTypeGetRequest);
    }

    public boolean checkPropertyUsageTypeExists(final PropertyTypeUsageType propUsageType) {
        getPropertyTypeByName(propUsageType);
        getUsageTypeByName(propUsageType);
        return propUsageTypeRepository.checkPropertyUsageTypeExists(
                propUsageType.getCode(),
                propUsageType.getPropertyTypeId(),
                propUsageType.getUsageTypeId(),
                propUsageType.getTenantId());
    }

    public Boolean getPropertyTypeByName(final PropertyTypeUsageType propUsageType) {
        Boolean isValidProperty = Boolean.FALSE;

        final PropertyTypeResponse propertyType = restExternalMasterService.getPropertyIdFromPTModule(
                propUsageType.getPropertyType(),
                propUsageType.getTenantId());
        if (propertyType.getPropertyTypesSize()) {
            isValidProperty = Boolean.TRUE;
            propUsageType.setPropertyTypeId(
                    propertyType.getPropertyTypes() != null && propertyType.getPropertyTypes().get(0) != null
                            ? propertyType.getPropertyTypes().get(0).getId() : "");

        }
        return isValidProperty;

    }

    public Boolean getUsageTypeByName(final PropertyTypeUsageType propUsageType) {
        Boolean isValidUsage = Boolean.FALSE;
        final UsageTypeResponse usageType = restExternalMasterService.getUsageIdFromPTModule(
                propUsageType.getUsageType(),WcmsConstants.WC,
                propUsageType.getTenantId());
        if (usageType.getUsageTypesSize()) {
            isValidUsage = Boolean.TRUE;
            propUsageType
                    .setUsageTypeId(usageType.getUsageMasters() != null && usageType.getUsageMasters().get(0) != null
                            ? usageType.getUsageMasters().get(0).getId() : "");
            propUsageType.setUsageCode(usageType.getUsageMasters() != null && usageType.getUsageMasters().get(0) != null ?usageType.getUsageMasters().get(0).getCode():"");
            propUsageType.setService(usageType.getUsageMasters() != null && usageType.getUsageMasters().get(0) != null ?usageType.getUsageMasters().get(0).getService():"");

        }
        return isValidUsage;

    }

}
