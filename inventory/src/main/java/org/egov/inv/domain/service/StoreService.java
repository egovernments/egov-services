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

import org.egov.inv.domain.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreService {

	@Autowired
	private InventoryUtilityService inventoryUtilityService;

	@Autowired
	private StoreRepository storeRepository;

	public List<Store> create(StoreRequest storeRequest, String tenantId) {
		List<Long> storesIdList = inventoryUtilityService.getIdList(storeRequest.getStores().size(), "seq_stores");
		for (int i = 0; i <= storeRequest.getStores().size() - 1; i++) {
			storeRequest.getStores().get(i).setId(storesIdList.get(i).toString());
			storeRequest.getStores().get(i)
					.setAuditDetails(inventoryUtilityService.mapAuditDetails(storeRequest.getRequestInfo(), tenantId));
		}
		return storeRepository.create(storeRequest);
	}

	public List<Store> update(StoreRequest storeRequest, String tenantId) {
		List<Store> storesList = storeRequest.getStores();
		for (int i = 0; i <= storesList.size() - 1; i++) {
			storeRequest.getStores().get(i).setAuditDetails(
					inventoryUtilityService.mapAuditDetailsForUpdate(storeRequest.getRequestInfo(), tenantId));
		}
		return storeRepository.update(storeRequest);
	}

	public Pagination<Store> search(StoreGetRequest storeGetRequest) {
		return storeRepository.search(storeGetRequest);
	}

}
