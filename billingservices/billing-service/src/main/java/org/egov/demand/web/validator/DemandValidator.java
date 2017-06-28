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
package org.egov.demand.web.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.egov.demand.model.Demand;
import org.egov.demand.model.DemandDetail;
import org.egov.demand.model.Owner;
import org.egov.demand.model.TaxHeadMaster;
import org.egov.demand.model.TaxHeadMasterCriteria;
import org.egov.demand.repository.OwnerRepository;
import org.egov.demand.service.TaxHeadMasterService;
import org.egov.demand.web.contract.DemandRequest;
import org.egov.demand.web.contract.UserSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class DemandValidator implements Validator {
	
	@Autowired
	private TaxHeadMasterService taxHeadMasterService;

	@Autowired
	private OwnerRepository ownerRepository;

	@Override
	public boolean supports(Class<?> clazz) {

		return DemandRequest.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

		DemandRequest demandRequest = null;
		if (target instanceof DemandRequest)
			demandRequest = (DemandRequest) target;
		else
			throw new RuntimeException("Invalid Object type for Demand validator");
		validateOwner(demandRequest, errors);
		validateTaxHeadMaster(demandRequest, errors);
		validateBusinessService(demandRequest, errors);
		validateTaxPeriod(demandRequest, errors);
	}

	private void validateTaxPeriod(DemandRequest demandRequest, Errors errors) {
		// TODO Auto-generated method stub

	}

	private void validateBusinessService(DemandRequest demandRequest, Errors errors) {
		
		
	}

	private void validateTaxHeadMaster(DemandRequest demandRequest, Errors errors) {

		Set<String> codes = new HashSet<>();
		for (Demand demand : demandRequest.getDemands()) {
			for (DemandDetail demandDetail : demand.getDemandDetails()) {
				codes.add(demandDetail.getTaxHeadMasterCode());
			}
		}
		TaxHeadMasterCriteria taxHeadMasterCriteria = new TaxHeadMasterCriteria();
		taxHeadMasterCriteria.setTenantId(demandRequest.getDemands().get(0).getTenantId());
		taxHeadMasterCriteria.setCode(codes);
		Map<String, String> codesMap = new HashMap<>();
		for (TaxHeadMaster taxHeadMaster : taxHeadMasterService
				.getTaxHeads(taxHeadMasterCriteria, demandRequest.getRequestInfo()).getTaxHeadMasters()) {
			codesMap.put(taxHeadMaster.getCode(), taxHeadMaster.getCode());
		}
		for (String code : codes) {
			if (codesMap.get(code) == null)
				errors.rejectValue("Demand.DemandDetail.code", "",
						"the given code value '" + code + "'of teaxheadmaster is invalid, please give a valid code");
		}
	}

	private void validateOwner(DemandRequest demandRequest, Errors errors) {

		List<Demand> demands = demandRequest.getDemands();
		List<Long> ownerIds = new ArrayList<>(
				demands.stream().map(demand -> demand.getOwner().getId()).collect(Collectors.toSet()));
		UserSearchRequest userSearchRequest = new UserSearchRequest();
		userSearchRequest.setId(ownerIds);
		Map<Long, Long> ownerMap = new HashMap<>();
		for (Owner owner : ownerRepository.getOwners(userSearchRequest)) {
			ownerMap.put(owner.getId(), owner.getId());
		}
		for (Long rsId : ownerIds) {
			if (ownerMap.get(rsId) == null)
				errors.rejectValue("Demand.Owner.id", "",
						"the given user id value '" + rsId + "' is invalid, please give a valid user id");
		}
	}

}
