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

import java.util.List;

import org.egov.inv.domain.model.SupplierGetRequest;
import org.egov.inv.persistence.entity.SupplierEntity;
import org.egov.inv.persistence.repository.SupplierESRepository;
import org.egov.inv.persistence.repository.SupplierJdbcRepository;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import io.swagger.model.Pagination;
import io.swagger.model.Supplier;
import io.swagger.model.SupplierRequest;

@Service
public class SupplierService {

	@Autowired
	private InventoryUtilityService inventoryUtilityService;

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

	@Value("${es.enabled}")
	private Boolean isESEnabled;

	public List<Supplier> create(SupplierRequest supplierRequest,
			String tenantId, BindingResult errors) {

		for (Supplier supplier : supplierRequest.getSuppliers()) {
			supplier.setAuditDetails(inventoryUtilityService.mapAuditDetails(
					supplierRequest.getRequestInfo(), tenantId));
			if (!supplierJdbcRepository.uniqueCheck("code",
					new SupplierEntity().toEntity(supplier))) {
				throw new CustomException("inv.004",
						"supplier code and tenantId combination should be unique");
			}
			supplier.setId(supplierJdbcRepository.getSequence(supplier));
		}
		return push(supplierRequest, createTopic);
	}

	public List<Supplier> push(SupplierRequest supplierRequest, String topic) {
		kafkaTemplate.send(topic, supplierRequest);
		return supplierRequest.getSuppliers();
	}

	public List<Supplier> update(SupplierRequest supplierRequest,
			String tenantId, BindingResult errors) {

		for (Supplier supplier : supplierRequest.getSuppliers()) {
			if (supplier.getId() == null) {
				throw new CustomException("inv.005",
						"Id is mandatory during updation");
			}
			supplier.setAuditDetails(
					inventoryUtilityService.mapAuditDetailsForUpdate(
							supplierRequest.getRequestInfo(), tenantId));
			if (!supplierJdbcRepository.uniqueCheck("code",
					new SupplierEntity().toEntity(supplier))) {
				throw new CustomException("inv.004",
						"supplier code and tenantId combination should be unique");
			}
		}

		return push(supplierRequest, updateTopic);
	}

	public Pagination<Supplier> search(SupplierGetRequest supplierGetRequest) {
		return isESEnabled ? supplierESRepository.search(supplierGetRequest)
				: supplierJdbcRepository.search(supplierGetRequest);

	}

}
