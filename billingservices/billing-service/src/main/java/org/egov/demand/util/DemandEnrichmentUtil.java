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
package org.egov.demand.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.egov.demand.config.Config;
import org.egov.demand.model.Demand;
import org.egov.demand.model.DemandDetail;
import org.egov.demand.model.Owner;
import org.egov.demand.model.TaxHeadMaster;
import org.egov.demand.model.TaxHeadMasterCriteria;
import org.egov.demand.repository.TaxHeadMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DemandEnrichmentUtil {

	@Autowired
	TaxHeadMasterRepository taxHeadMasterRepository;

	@Autowired
	private Config config;

	public List<Demand> enrichOwners(List<Demand> demands, List<Owner> owners) {

		Map<Long, Owner> map = new HashMap<>();
		List<Demand> rsDemands = new ArrayList<>();
		for (Owner owner : owners) {
			map.put(owner.getId(), owner);
		}
		for (Demand demand : demands) {
			Long ownerId = demand.getOwner().getId();
			if (map.containsKey(ownerId)) {
				demand.setOwner(map.get(ownerId));
				rsDemands.add(demand);
			}
		}
		return rsDemands;
	}

	/**
	 *
	 * @param demands
	 * @return
	 */
	public List<Demand> ceilDecimal(List<Demand> demands) {

		String tenantId = demands.get(0).getTenantId();
		String service = demands.get(0).getBusinessService();

		Map<String, Boolean> isTaxHeadDebitMap = taxHeadMasterRepository.findForCriteria(TaxHeadMasterCriteria.builder().tenantId(tenantId)
				.service(service).build()).stream().collect(Collectors.toMap(TaxHeadMaster::getCode, TaxHeadMaster::getIsDebit));



		for (Demand demand : demands) {

			BigDecimal totalTax = BigDecimal.ZERO;
			BigDecimal collectedTax = BigDecimal.ZERO;
			final List<DemandDetail> details = demand.getDemandDetails();
			final List<Integer> indicesToBeRemoved = new ArrayList<>();

			for (DemandDetail detail : details) {

				collectedTax = collectedTax.add(detail.getCollectionAmount());
				if (detail.getTaxHeadMasterCode().toLowerCase().contains("decimal")) {
					indicesToBeRemoved.add(details.indexOf(detail));
				}
				else if (!isTaxHeadDebitMap.get(detail.getTaxHeadMasterCode()))
					totalTax = totalTax.add(detail.getTaxAmount());

				else
					totalTax = totalTax.subtract(detail.getTaxAmount());

			}

			DemandDetail newDetail= null;
			try {
			 newDetail= roundOfDecimals(totalTax.subtract(collectedTax), service);
			}catch(RuntimeException rte) {
				continue;
			}

			System.err.println(" the new detail : " + newDetail);
			if(null != newDetail)
				demand.getDemandDetails().add(newDetail);

			indicesToBeRemoved.forEach(index -> details.remove(index.intValue()));
		}
		return demands;
	}

	/**
	 * Decimal is ceiled for all the tax heads
	 *
	 * if the decimal is greater than 0.5 upper bound will be applied
	 *
	 * else if decimal is lesser than 0.5 lower bound is applied
	 *
	 * @param estimates
	 */
	public DemandDetail roundOfDecimals(BigDecimal result, String service) {

		Map<String, String> creditMap = config.getCreditDecimalMap();
		Map<String, String> debitMap  = config.getDebitDecimalMap();

		if( creditMap.get(service) == null || debitMap.get(service) == null)
			throw new RuntimeException(" No decimal tax heads found for this service");

		BigDecimal roundOffAmount = result.setScale(2, 2);
		BigDecimal reminder = roundOffAmount.remainder(BigDecimal.ONE);

		if(reminder.doubleValue() == 0.0)
			return null;

		else if (reminder.doubleValue() >= 0.5)
			return DemandDetail.builder().taxHeadMasterCode(creditMap.get(service))
					.taxAmount(BigDecimal.ONE.subtract(reminder)).build();

		else
			return DemandDetail.builder().taxHeadMasterCode(debitMap.get(service))
					.taxAmount(reminder).build();
	}

}
