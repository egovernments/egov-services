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

import org.egov.wcms.config.ConfigurationManager;
import org.egov.wcms.model.PropertyTypePipeSizeType;
import org.egov.wcms.producers.WaterMasterProducer;
import org.egov.wcms.repository.PropertyPipeSizeRepository;
import org.egov.wcms.web.contract.PropertyTypePipeSizeTypeGetRequest;
import org.egov.wcms.web.contract.PropertyTypePipeSizeTypeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PropertyTypePipeSizeTypeService {

    public static final Logger logger = LoggerFactory.getLogger(PropertyTypePipeSizeTypeService.class);

    @Autowired
    private PropertyPipeSizeRepository propertyPipeSizeRepository;

    @Autowired
    private WaterMasterProducer waterMasterProducer;
   

    public PropertyTypePipeSizeTypeRequest create(final PropertyTypePipeSizeTypeRequest propertyPipeSizeRequest) {
        return propertyPipeSizeRepository.persistCreatePropertyPipeSize(propertyPipeSizeRequest);
    }

    public PropertyTypePipeSizeTypeRequest update(final PropertyTypePipeSizeTypeRequest propertyPipeSizeRequest) {
        return propertyPipeSizeRepository.persistUpdatePropertyPipeSize(propertyPipeSizeRequest);
    }

    public PropertyTypePipeSizeType createPropertyPipeSize(final String topic, final String key,
            final PropertyTypePipeSizeTypeRequest propertyPipeSizeRequest) {
        final ObjectMapper mapper = new ObjectMapper();
        String propertyPipeSizeValue = null;
        try {
            logger.info("createPropertyPipeSize service::" + propertyPipeSizeRequest);
            propertyPipeSizeValue = mapper.writeValueAsString(propertyPipeSizeRequest);
            logger.info("propertyPipeSizeValue::" + propertyPipeSizeValue);
        } catch (final JsonProcessingException e) {
            logger.error("Exception Encountered : " + e);
        }
        try {
            waterMasterProducer.sendMessage(topic, key, propertyPipeSizeValue);
        } catch (final Exception ex) {
            logger.error("Exception Encountered : " + ex);
        }
        return propertyPipeSizeRequest.getPropertyPipeSize();
    }

    public boolean checkPropertyByPipeSize(final Long id, final Long properyTypeId, final Long pipeSizeId,
            final String tenantId) {
        return propertyPipeSizeRepository.checkPropertyByPipeSize(id, properyTypeId, pipeSizeId, tenantId);
    }

    public List<PropertyTypePipeSizeType> getPropertyPipeSizes(
            final PropertyTypePipeSizeTypeGetRequest propertyPipeSizeGetRequest) {
        return propertyPipeSizeRepository.findForCriteria(propertyPipeSizeGetRequest);

    }

    public boolean checkPipeSizeExists(final Double pipeSizeType, final String tenantId) {
        return propertyPipeSizeRepository.checkPipeSizeExists(pipeSizeType, tenantId);
    }

}
