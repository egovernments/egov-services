package org.egov.lams.services.service.persistence.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.contract.request.RequestInfo;
import org.egov.lams.services.config.PropertiesManager;
import org.egov.mdms.model.MasterDetail;
import org.egov.mdms.model.MdmsResponse;
import org.egov.mdms.service.MdmsClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import net.minidev.json.JSONArray;

@Repository
public class MasterDataRepository {

	@Autowired
	private PropertiesManager appProps;

	@Autowired
	private MdmsClientService mdmsClientService;

	public Map<String, JSONArray> getMastersByCode(Map<String, String> codes, RequestInfo requestInfo,
			String tenantId) {

		String ModuleName = appProps.getMdmsModuleName();

		Map<String, List<MasterDetail>> map = new HashMap<>();
		List<MasterDetail> masterDetails = new ArrayList<>();

		for (String key : codes.keySet()) 
				masterDetails.add(MasterDetail.builder().name(key).filter("[?( @.code == '" + codes.get(key) + "')]").build());

		map.put(ModuleName, masterDetails);
		MdmsResponse mdmsResponse = mdmsClientService.getMaster(requestInfo, tenantId, map);
		return mdmsResponse.getMdmsRes().get(ModuleName);
	}
	
	public Map<String, JSONArray> getEstateMastersCodesForSearch(Map<String, String> codes, RequestInfo requestInfo,
			String tenantId) {

		String ModuleName = appProps.getMdmsModuleName();

		Map<String, List<MasterDetail>> map = new HashMap<>();
		List<MasterDetail> masterDetails = new ArrayList<>();

		for (String key : codes.keySet()) {
			if(key.equalsIgnoreCase(appProps.getMdmsRegisterName()))
				masterDetails.add(MasterDetail.builder().name(key).filter("[?( @.registerName == '" + codes.get(key) + "')]").build());
			else if(key.equalsIgnoreCase(appProps.getMdmsSubRegisterName()))
				masterDetails.add(MasterDetail.builder().name(key).filter("[?( @.subRegister == '" + codes.get(key) + "')]").build());
			else if(key.equalsIgnoreCase(appProps.getMdmsPropertyType()))
				masterDetails.add(MasterDetail.builder().name(key).filter("[?( @.name == '" + codes.get(key) + "')]").build());
		}
		
		map.put(ModuleName, masterDetails);
		MdmsResponse mdmsResponse = mdmsClientService.getMaster(requestInfo, tenantId, map);
		return mdmsResponse.getMdmsRes().get(ModuleName);
	}
}
