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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.demand.model.Demand;
import org.egov.demand.model.Owner;
import org.springframework.stereotype.Component;

@Component
public class DemandEnrichmentUtil {

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
			}
			rsDemands.add(demand);
		}
		return rsDemands;
	}

	/*public void enrichTaxHeadMAsters(List<DemandDetail> demandDetails, List<TaxHeadMaster> taxHeadMAsters) {

		System.err.println("taxheadmaster list : "+taxHeadMAsters);
		System.err.println("demanddetails list : "+demandDetails);

		Map<String,TaxHeadMaster> map = new HashMap<>();
		for (TaxHeadMaster taxHeadMaster : taxHeadMAsters) {
			map.put(taxHeadMaster.getCode(), taxHeadMaster);
		}
		
		for (DemandDetail demandDetail : demandDetails) {
			String code = demandDetail.getTaxHeadMaster().getCode();
			if(map.containsKey(code))
				demandDetail.setTaxHeadMaster(map.get(code));
		}
	}*/
}
