package org.egov.demand.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.demand.model.Demand;
import org.egov.demand.model.DemandDetail;
import org.egov.demand.model.Owner;
import org.egov.demand.model.TaxHeadMaster;
import org.springframework.stereotype.Component;

@Component
public class DemandEnrichmentUtil {

	public List<Demand> enrichOwners(List<Demand> demands,List<Owner> owners){
		
		Map<Long,Owner> map = new HashMap<>();
		List<Demand> rsDemands = new ArrayList<>();
		for (Owner owner : owners) {
			map.put(owner.getId(),owner);
		}
		
		for (Demand demand : demands) {
			
			Long ownerId = demand.getOwner().getId();
			if(map.containsKey(ownerId)){
				demand.setOwner(map.get(ownerId));
				rsDemands.add(demand);
			}
		}
	return rsDemands;
	}

	public void enrichTaxHeadMAsters(List<DemandDetail> demandDetails, List<TaxHeadMaster> taxHeadMAsters) {

		Map<String,TaxHeadMaster> map = new HashMap<>();
		for (TaxHeadMaster taxHeadMaster : taxHeadMAsters) {
			map.put(taxHeadMaster.getCode(), taxHeadMaster);
		}
		
		for (DemandDetail demandDetail : demandDetails) {
			String code = demandDetail.getTaxHeadMaster().getCode();
			if(map.containsKey(code))
				demandDetail.setTaxHeadMaster(map.get(code));
		}
	}
}
