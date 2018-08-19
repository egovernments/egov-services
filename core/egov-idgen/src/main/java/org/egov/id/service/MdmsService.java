package org.egov.id.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.id.model.RequestInfo;
import org.egov.mdms.model.MasterDetail;
import org.egov.mdms.model.MdmsCriteria;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.mdms.model.MdmsResponse;
import org.egov.mdms.model.ModuleDetail;
import org.egov.mdms.service.MdmsClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class MdmsService {

	@Autowired
	MdmsClientService mdmsClientService;

	private static final String tenantMaster = "tenants";

	private static final String tenantModule = "tenant";

	public MdmsResponse getMasterData(RequestInfo requestInfo, String tenantId,
			Map<String, List<MasterDetail>> masterDetails) {

		MdmsResponse mdmsResponse = null;
		try {
			mdmsResponse = mdmsClientService.getMaster(RequestInfo.toCommonRequestInfo(requestInfo), tenantId,
					masterDetails);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mdmsResponse;
	}

	public String getCity(RequestInfo requestInfo, String tenantId) {
		
		Map<String, List<MasterDetail>> masterDetails = new HashMap<String, List<MasterDetail>>();
		MasterDetail masterDetail = MasterDetail.builder().name(tenantMaster)
				.filter("[?(@.code==" + "'" + tenantId + "'" + ")]").build();
		masterDetails.put(tenantModule, Arrays.asList(masterDetail));

		MdmsResponse mdmsResponse = null;
		String cityCode = null;
		try {

			mdmsResponse = getMasterData(requestInfo, tenantId, masterDetails);
	
			if (mdmsResponse.getMdmsRes() != null && mdmsResponse.getMdmsRes().containsKey(tenantModule)
					&& mdmsResponse.getMdmsRes().get(tenantModule).containsKey(tenantMaster)) {
				DocumentContext documentContext = JsonPath
						.parse(mdmsResponse.getMdmsRes().get(tenantModule));
				cityCode = documentContext.read("$.tenants[0].city.code");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cityCode;
	}

	/**
	 * Prepares and returns Mdms search request with financial master criteria
	 * 
	 * @param requestInfo
	 * @param assesmentYear
	 * @return
	 */
	private MdmsResponse doMdmsServiceCall(RequestInfo requestInfo, MdmsCriteria mdmsCriteria) {

		/*
		 * MasterDetail mstrDetail = MasterDetail.builder().name("tenants")
		 * .filter("[?(@.code=="+"'"+tenantId+"'"+")]") .build(); ModuleDetail
		 * moduleDetail = ModuleDetail.builder().moduleName("tenant")
		 * .masterDetails(Arrays.asList(mstrDetail)).build(); MdmsCriteria
		 * mdmsCriteria =
		 * MdmsCriteria.builder().moduleDetails(Arrays.asList(moduleDetail)).
		 * tenantId(tenantId) .build();
		 */
		MdmsCriteriaReq mdmsCriteriaReq = null;
		MdmsResponse mdmsResponse = null;
		try {
			mdmsCriteriaReq = MdmsCriteriaReq.builder().requestInfo(RequestInfo.toCommonRequestInfo(requestInfo))
					.mdmsCriteria(mdmsCriteria).build();
			mdmsResponse = mdmsClientService.getMaster(mdmsCriteriaReq);
			if (mdmsResponse.getMdmsRes() != null && mdmsResponse.getMdmsRes().containsKey("tenant")
					&& mdmsResponse.getMdmsRes().get("tenant").containsKey("tenants")) {
				DocumentContext documentContext = JsonPath
						.parse(mdmsResponse.getMdmsRes().get("tenant").get("tenants"));
				System.out.println(documentContext.jsonString());
				String res = documentContext.read("$.MdmsRes.tenant.tenants[0].city.code");
				System.out.println(res);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		mdmsResponse.getMdmsRes().get("tenant").get("tenants");

		return null;
	}

}
