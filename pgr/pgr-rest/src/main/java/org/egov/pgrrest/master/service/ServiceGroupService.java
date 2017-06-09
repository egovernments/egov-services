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
package org.egov.pgrrest.master.service;


import org.egov.pgrrest.master.model.ServiceGroup;
import org.egov.pgrrest.master.producers.PGRProducer;
import org.egov.pgrrest.master.repository.ServiceGroupRepository;
import org.egov.pgrrest.master.web.contract.ServiceGroupRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ServiceGroupService {

    public static final Logger logger = LoggerFactory.getLogger(ServiceGroupService.class);

    @Autowired
    private ServiceGroupRepository categoryRepository;

    @Autowired
    private PGRProducer pgrProducer;


    public ServiceGroupRequest create(final ServiceGroupRequest categoryRequest) {
    	logger.info("Persisting category type record");
        return categoryRepository.persistCreateCategory(categoryRequest);
    }

 /*   public CategoryTypeRequest update(final CategoryTypeRequest categoryRequest) {
        return categoryRepository.persistModifyCategory(categoryRequest);
    } */

    public ServiceGroup createCategory(final ServiceGroupRequest serviceGroupRequest) {
        final ObjectMapper mapper = new ObjectMapper();
        String categoryValue = null;
        String topic = "egov.pgr.categorytype-create";
        String key = "categorytype-create";
        try {
            logger.info("ServiceGroupRequest::" + serviceGroupRequest);
            categoryValue = mapper.writeValueAsString(serviceGroupRequest);
            logger.info("categoryValue::" + categoryValue);
        } catch (final JsonProcessingException e) {
            logger.error("Exception Encountered : " + e);
        }
        try {
            pgrProducer.sendMessage(topic, key, categoryValue);
        } catch (final Exception ex) {
            logger.error("Exception Encountered : " + ex);
        }
        return serviceGroupRequest.getServiceGroup();
    }

 /*   public CategoryType updateCategory(final String topic, final String key,
            final CategoryTypeRequest categoryRequest) {
        final ObjectMapper mapper = new ObjectMapper();
        String categoryValue = null;
        try {
            logger.info("updateCategory service::" + categoryRequest);
            categoryValue = mapper.writeValueAsString(categoryRequest);
            logger.info("categoryValue::" + categoryValue);
        } catch (final JsonProcessingException e) {
            logger.error("Exception Encountered : " + e);
        }
        try {
            waterMasterProducer.sendMessage(topic, key, categoryValue);
        } catch (final Exception ex) {
            logger.error("Exception Encountered : " + ex);
        }
        return categoryRequest.getCategory();
    }

    public boolean getCategoryByNameAndCode(final String code, final String name, final String tenantId) {
        return categoryRepository.checkCategoryByNameAndCode(code, name, tenantId);
    }

    public List<CategoryType> getCategories(final CategoryTypeGetRequest categoryGetRequest) {
        return categoryRepository.findForCriteria(categoryGetRequest);

    } */

}
