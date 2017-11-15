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

import io.swagger.model.Pagination;
import io.swagger.model.Store;
import io.swagger.model.StoreGetRequest;
import io.swagger.model.StoreRequest;

import org.egov.inv.domain.exception.ErrorCode;
import org.egov.inv.domain.exception.InvalidDataException;
import org.egov.inv.persistence.entity.StoreEntity;
import org.egov.inv.persistence.repository.StoreESRepository;
import org.egov.inv.persistence.repository.StoreJdbcRepository;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import java.util.List;

@Service
public class StoreService {

	@Autowired
	private StoreJdbcRepository storeJdbcRepository;

	@Value("${inv.store.save.topic}")
	private String createTopic;

	@Value("${inv.store.update.topic}")
	private String updateTopic;
	
	@Value("${es.enabled}")
	private Boolean isESEnabled;

	@Autowired
	private InventoryUtilityService inventoryUtilityService;

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;
	
	@Autowired
	private StoreESRepository storeESRepository;

	public List<Store> create(StoreRequest storeRequest, String tenantId,
			BindingResult errors) {

		for (Store store : storeRequest.getStores()) {
			store.setAuditDetails(inventoryUtilityService
					.mapAuditDetails(storeRequest.getRequestInfo(), tenantId));
			if (!storeJdbcRepository.uniqueCheck("code",
					new StoreEntity().toEntity(store))) {
				throw new CustomException("inv.003",
						"Store code should be unique");
			}
			store.setId(storeJdbcRepository.getSequence(store));
		}

		return push(storeRequest, createTopic);
	}

	public List<Store> update(StoreRequest storeRequest, String tenantId,
			BindingResult errors) {

		for (Store store : storeRequest.getStores()) {
			if (store.getId() == null) {
				throw new InvalidDataException("id",
						ErrorCode.MANDATORY_VALUE_MISSING.getCode(),
						store.getId());
			}
			store.setAuditDetails(
					inventoryUtilityService.mapAuditDetailsForUpdate(
							storeRequest.getRequestInfo(), tenantId));
			if (!storeJdbcRepository.uniqueCheck("code",
					new StoreEntity().toEntity(store))) {
				throw new CustomException("inv.003",
						"Store code should be unique");
			}
		}

		return push(storeRequest, updateTopic);
	}

	public Pagination<Store> search(StoreGetRequest storeGetRequest) {
		
	//	return isESEnabled ? storeESRepository.search(storeGetRequest):
			return	storeJdbcRepository.search(storeGetRequest);
	}

	public List<Store> push(StoreRequest storeRequest, String topic) {
		kafkaTemplate.send(topic, storeRequest);
		return storeRequest.getStores();
	}

}
