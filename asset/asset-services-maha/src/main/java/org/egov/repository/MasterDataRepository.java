package org.egov.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.contract.request.RequestInfo;
import org.egov.config.ApplicationProperties;
import org.egov.mdms.model.MasterDetail;
import org.egov.mdms.model.MdmsResponse;
import org.egov.mdms.service.MdmsClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import net.minidev.json.JSONArray;

@Repository
public class MasterDataRepository {

	@Autowired
	private ApplicationProperties appProps;

	@Autowired
	private MdmsClientService mdmsClientService;

	public Map<String, JSONArray> getAssetMastersById(Map<String, String> idList, RequestInfo requestInfo,
			String tenantId) {

		String ModuleName = appProps.getMdMsModuleName();

		Map<String, List<MasterDetail>> map = new HashMap<>();
		List<MasterDetail> masterDetails = new ArrayList<>();

		for (String key : idList.keySet()) {
			masterDetails.add(MasterDetail.builder().name(key).filter("[?( @.id in [" + idList.get(key) + "])]").build());
		}
		map.put(ModuleName, masterDetails);
		MdmsResponse mdmsResponse = mdmsClientService.getMaster(requestInfo, tenantId, map);

		return mdmsResponse.getMdmsRes().get(ModuleName);
	}
}
