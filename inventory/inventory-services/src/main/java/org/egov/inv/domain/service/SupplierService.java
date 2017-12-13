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
import org.egov.common.Pagination;
import org.egov.common.exception.CustomBindException;
import org.egov.common.exception.ErrorCode;
import org.egov.common.exception.InvalidDataException;
import org.egov.inv.model.*;
import org.egov.inv.persistence.entity.SupplierEntity;
import org.egov.inv.persistence.repository.SupplierESRepository;
import org.egov.inv.persistence.repository.SupplierJdbcRepository;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.util.StringUtils.isEmpty;


@Service
public class SupplierService extends DomainService {

    @Autowired
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private SupplierESRepository supplierESRepository;

    @Autowired
    private SupplierJdbcRepository supplierJdbcRepository;

    @Value("${inv.supplier.save.topic}")
    private String createTopic;

    @Value("${inv.supplier.update.topic}")
    private String updateTopic;

    @Value("${inv.supplier.save.key}")
    private String createKey;

    @Value("${inv.supplier.update.key}")
    private String updateKey;

    @Value("${es.enabled}")
    private Boolean isESEnabled;

    @Autowired
    private BankContractRepository bankContractRepository;

    public SupplierResponse create(SupplierRequest supplierRequest, String tenantId) {
        try {
            SupplierRequest fetchRelated = fetchRelated(supplierRequest, tenantId);
            validate(fetchRelated.getSuppliers(), Constants.ACTION_CREATE, tenantId);
            List<String> sequenceNos = supplierJdbcRepository.getSequence(Supplier.class.getSimpleName(), supplierRequest.getSuppliers().size());
            int i = 0;
            for (Supplier supplier : supplierRequest.getSuppliers()) {
                if (isEmpty(supplier.getTenantId())) {
                    supplier.setTenantId(tenantId);
                }
                supplier.setId(sequenceNos.get(i));
                supplier.setStatus(Supplier.StatusEnum.ACTIVE);
                i++;
                supplier.setAuditDetails(mapAuditDetails(
                        supplierRequest.getRequestInfo()));
            }
            kafkaTemplate.send(createTopic, createKey, supplierRequest);
            SupplierResponse response = new SupplierResponse();
            response.setSuppliers(supplierRequest.getSuppliers());
            response.setResponseInfo(getResponseInfo(supplierRequest.getRequestInfo()));
            return response;
        } catch (CustomBindException e) {
            throw e;
        }
    }

    public SupplierResponse update(SupplierRequest supplierRequest, String tenantId) {

        try {
            SupplierRequest fetchRelated = fetchRelated(supplierRequest, tenantId);
            validate(fetchRelated.getSuppliers(), Constants.ACTION_UPDATE, tenantId);

            for (Supplier supplier : supplierRequest.getSuppliers()) {
                if (isEmpty(supplier.getTenantId())) {
                    supplier.setTenantId(tenantId);
                }
                if (!supplier.getActive()) {
                    supplier.setStatus(Supplier.StatusEnum.INACTIVE);
                }
                supplier.setAuditDetails(mapAuditDetailsForUpdate(supplierRequest.getRequestInfo()));
            }
            kafkaTemplate.send(updateTopic, updateKey, supplierRequest);
            SupplierResponse response = new SupplierResponse();
            response.setSuppliers(supplierRequest.getSuppliers());
            response.setResponseInfo(getResponseInfo(supplierRequest.getRequestInfo()));
            return response;
        } catch (CustomBindException e) {
            throw e;
        }

    }

    private void validate(List<Supplier> suppliers, String method, String tenantId) {
        InvalidDataException errors = new InvalidDataException();

        try {
            switch (method) {

                case Constants.ACTION_CREATE:
                    if (suppliers == null) {
                        throw new InvalidDataException("suppliers", ErrorCode.NOT_NULL.getCode(), null);
                    }
                    for (Supplier supplier : suppliers) {
                        if (isEmpty(supplier.getTenantId())) {
                            supplier.setTenantId(tenantId);
                        }
                        if (!supplierJdbcRepository.uniqueCheck("code",
                                new SupplierEntity().toEntity(supplier))) {
                            errors.addDataError(ErrorCode.CODE_ALREADY_EXISTS.getCode(), "Supplier", supplier.getCode());

                        }
                        validateBank(supplier, errors);
                    }
                    break;
                case Constants.ACTION_UPDATE:
                    if (suppliers == null) {
                        throw new InvalidDataException("suppliers", ErrorCode.NOT_NULL.getCode(), null);
                    }
                    for (Supplier supplier : suppliers) {
                        if (isEmpty(supplier.getTenantId())) {
                            supplier.setTenantId(tenantId);
                        }
                        if (supplier.getId() == null) {
                            throw new InvalidDataException("id", ErrorCode.MANDATORY_VALUE_MISSING.getCode(), supplier.getId());
                        }
                        if (!supplierJdbcRepository.uniqueCheck("code",
                                new SupplierEntity().toEntity(supplier))) {
                            errors.addDataError(ErrorCode.CODE_ALREADY_EXISTS.getCode(), "Supplier", supplier.getCode());

                        }
                        validateBank(supplier, errors);

                    }
            }
        } catch (IllegalArgumentException e) {

        }
        if (errors.getValidationErrors().size() > 0)
            throw errors;
    }


    public SupplierResponse search(SupplierGetRequest supplierGetRequest) {
        SupplierResponse supplierResponse = new SupplierResponse();
        //isESEnabled ? supplierESRepository.search(supplierGetRequest):
        Pagination<Supplier> search = supplierJdbcRepository.search(supplierGetRequest);
        supplierResponse.setSuppliers(search.getPagedData());
        return supplierResponse;
    }

    private SupplierRequest fetchRelated(SupplierRequest supplierRequest, String tenantId) {

        List<Supplier> suppliers = supplierRequest.getSuppliers();

        for (Supplier supplier : suppliers) {
        	if(supplier.getCode()!=null)
        	supplier.setCode(supplier.getCode().toUpperCase());
            BankContract bankContract = new BankContract();
            bankContract.setCode(supplier.getBankCode());
            bankContract.setTenantId(!isEmpty(supplier.getTenantId()) ? supplier.getTenantId() : tenantId);
            BankContract bank = bankContractRepository.findByCode(bankContract);
            supplier.setBankCode(bank.getCode());
        }

        return supplierRequest;
    }

    private void validateBank(Supplier supplier, InvalidDataException errors) {
        if (isEmpty(supplier.getBankCode())) {
            errors.addDataError(ErrorCode.CODE_ALREADY_EXISTS.getCode(), "Bank Code", supplier.getBankCode());

        }
    }
}
