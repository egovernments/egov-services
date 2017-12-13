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
package org.egov.inv.domain.service;


import org.egov.common.Constants;
import org.egov.common.DomainService;
import org.egov.common.MdmsRepository;
import org.egov.common.Pagination;
import org.egov.common.exception.CustomBindException;
import org.egov.common.exception.ErrorCode;
import org.egov.common.exception.InvalidDataException;
import org.egov.inv.model.*;
import org.egov.inv.persistence.entity.StoreEntity;
import org.egov.inv.persistence.repository.StoreJdbcRepository;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.util.StringUtils.isEmpty;

@Service
public class StoreService extends DomainService {

    @Autowired
    private StoreJdbcRepository storeJdbcRepository;

    @Value("${inv.store.save.topic}")
    private String createTopic;

    @Value("${inv.store.update.topic}")
    private String updateTopic;

    @Value("${inv.store.save.key}")
    private String createKey;

    @Value("${inv.store.update.key}")
    private String updateKey;

    @Value("${es.enabled}")
    private Boolean isESEnabled;

    @Autowired
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private MdmsRepository mdmsRepository;

    public StoreResponse create(StoreRequest storeRequest, String tenantId) {
        try {
            StoreRequest fetchRelated = fetchRelated(storeRequest, tenantId);
            validate(fetchRelated.getStores(), Constants.ACTION_CREATE, tenantId);
            List<String> sequenceNos = storeJdbcRepository.getSequence(Store.class.getSimpleName(), storeRequest.getStores().size());
            int i = 0;
            for (Store store : storeRequest.getStores()) {
                store.setId(sequenceNos.get(i));
                if (isEmpty(store.getTenantId())) {
                    store.setTenantId(tenantId);
                }
                i++;
                store.setAuditDetails(mapAuditDetails(
                        storeRequest.getRequestInfo()));
            }
            kafkaTemplate.send(createTopic, createKey, storeRequest);
            StoreResponse response = new StoreResponse();
            response.setStores(storeRequest.getStores());
            response.setResponseInfo(getResponseInfo(storeRequest.getRequestInfo()));
            return response;
        } catch (CustomBindException e) {
            throw e;
        }
    }

    public StoreResponse update(StoreRequest storeRequest, String tenantId) {

        try {
            StoreRequest fetchRelated = fetchRelated(storeRequest, tenantId);
            validate(fetchRelated.getStores(), Constants.ACTION_UPDATE, tenantId);
            for (Store store : storeRequest.getStores()) {
                if (isEmpty(store.getTenantId())) {
                    store.setTenantId(tenantId);
                }
                store.setAuditDetails(mapAuditDetailsForUpdate(storeRequest.getRequestInfo()));
            }
            kafkaTemplate.send(updateTopic, updateKey, storeRequest);
            StoreResponse response = new StoreResponse();
            response.setStores(storeRequest.getStores());
            response.setResponseInfo(getResponseInfo(storeRequest.getRequestInfo()));
            return response;
        } catch (CustomBindException e) {
            throw e;
        }
    }

    public StoreResponse search(StoreGetRequest storeGetRequest) {
        StoreResponse storeResponse = new StoreResponse();
        Pagination<Store> search = storeJdbcRepository.search(storeGetRequest);
        storeResponse.setStores(search.getPagedData());
        return storeResponse;
        //	return isESEnabled ? storeESRepository.search(storeGetRequest):
    }

    private void validate(List<Store> stores, String method, String tenantId) {
        InvalidDataException errors = new InvalidDataException();

        try {
            switch (method) {

                case Constants.ACTION_CREATE:
                    if (stores == null) {
                        throw new InvalidDataException("stores", ErrorCode.NOT_NULL.getCode(), null);
                    }
                    for (Store store : stores) {
                        if (isEmpty(store.getTenantId())) {
                            store.setTenantId(tenantId);
                        }
                        if (!storeJdbcRepository.uniqueCheck("code",
                                new StoreEntity().toEntity(store))) {
                            errors.addDataError(ErrorCode.CODE_ALREADY_EXISTS.getCode(), "Store", store.getCode());
                        }

                        validateStore(errors, store);
                    }

                    break;
                case Constants.ACTION_UPDATE:
                    if (stores == null) {
                        throw new InvalidDataException("stores", ErrorCode.NOT_NULL.getCode(), null);
                    }
                    for (Store store : stores) {
                        if (isEmpty(store.getTenantId())) {
                            store.setTenantId(tenantId);
                        }
                        if (store.getId() == null) {
                            throw new InvalidDataException("id", ErrorCode.MANDATORY_VALUE_MISSING.getCode(), store.getId());
                        }
                        store.setTenantId(tenantId);

                        if (!storeJdbcRepository.uniqueCheck("code",
                                new StoreEntity().toEntity(store))) {
                            errors.addDataError(ErrorCode.CODE_ALREADY_EXISTS.getCode(), "Store", store.getCode());

                        }

                        validateStore(errors, store);

                    }
            }
        } catch (IllegalArgumentException e) {

        }
        if (errors.getValidationErrors().size() > 0)
            throw errors;
    }

    private void validateStore(InvalidDataException errors, Store store) {
        if (null == store.getDepartment()) {
            errors.addDataError(ErrorCode.OBJECT_NOT_FOUND.getCode(), "Department ", "", "store " + store.getName());
        }

        if (null == store.getOfficeLocation()) {
            errors.addDataError(ErrorCode.OBJECT_NOT_FOUND.getCode(), "Office Location ", "", "store " + store.getName());
        }
    }

    private StoreRequest fetchRelated(StoreRequest storeRequest, String tenantId) {

        List<Store> stores = storeRequest.getStores();

        for (Store store : stores) {
            //fetch and set department
            if (null != store && null != store.getDepartment()) {
                Object object = mdmsRepository.fetchObject(tenantId, "common-masters", "Department", "code", store.getDepartment().getCode(), Department.class);
                store.setDepartment((Department) object);
            }

            //fetch and add office location
            if (null != store && null != store.getOfficeLocation() && !isEmpty(store.getOfficeLocation().getCode())) {
                Object object = mdmsRepository.fetchObject(tenantId, "inventory", "Location", "code", store.getOfficeLocation().getCode(), Location.class);

                store.setOfficeLocation((Location) object);
            }

        }

        return storeRequest;
    }

}
