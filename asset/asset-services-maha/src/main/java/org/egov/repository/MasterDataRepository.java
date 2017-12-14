package org.egov.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.egov.common.contract.request.RequestInfo;
import org.egov.config.ApplicationProperties;
import org.egov.contract.FinancialYear;
import org.egov.mdms.model.MasterDetail;
import org.egov.mdms.model.MdmsCriteria;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.mdms.model.MdmsResponse;
import org.egov.mdms.model.ModuleDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;

@Repository
@Slf4j
public class MasterDataRepository {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ApplicationProperties applicationProperties;

	public Map<String, Map<String, JSONArray>> getMastersByListParams(
			Map<String, Map<String, Map<String, String>>> moduleMap, RequestInfo requestInfo, String tenantId) {

		List<ModuleDetail> moduleDetailList = new ArrayList<>();

		// Map<String, List<MasterDetail>> map = new HashMap<>();

		for (String moduleKey : moduleMap.keySet()) {
			Map<String, Map<String, String>> masterMap = moduleMap.get(moduleKey);

			List<MasterDetail> masterDetails = new ArrayList<>();

			for (String masterKey : masterMap.keySet()) {

				Map<String, String> fieldMap = masterMap.get(masterKey);
				if (!fieldMap.isEmpty()) {
					StringBuilder filterString = new StringBuilder("[?( ");

					Set<String> fieldSet = fieldMap.keySet();
					String[] fieldSetArray = new String[fieldSet.size()];
					fieldSetArray = fieldSet.toArray(fieldSetArray);
					for (int index = 0; index < fieldSetArray.length; index++) {
						String fieldName = fieldSetArray[index];
						if (index != 0)
							filterString.append(" && ");
						filterString.append("@." + fieldName + " in [" + fieldMap.get(fieldName) + "]");
					}
					filterString.append(")]");
					masterDetails.add(MasterDetail.builder().name(masterKey).filter(filterString.toString()).build());
				} else
					masterDetails.add(MasterDetail.builder().name(masterKey).build());
			}
			// map.put(moduleKey, masterDetails);
			moduleDetailList.add(ModuleDetail.builder().moduleName(moduleKey).masterDetails(masterDetails).build());
		}

		MdmsCriteriaReq mdmsCriteriaReq = MdmsCriteriaReq.builder().requestInfo(requestInfo)
				.mdmsCriteria(MdmsCriteria.builder().moduleDetails(moduleDetailList).tenantId(tenantId).build())
				.build();

		return getMdmsResponse(mdmsCriteriaReq).getMdmsRes();
	}

	public FinancialYear getFinancialYears(Long toDate, RequestInfo requestInfo, String tenantId) {

		List<ModuleDetail> moduleDetailList = new ArrayList<>();
		List<MasterDetail> masterDetailList = new ArrayList<>();

		masterDetailList.add(MasterDetail.builder().name("financialYears")
				.filter("[?( @.startingDate <= " + toDate + " && @.endingDate >=" + toDate + ")]").build());

		moduleDetailList.add(ModuleDetail.builder().moduleName("egf-master").masterDetails(masterDetailList).build());
		
		if (!tenantId.equals("default"))
			tenantId = tenantId.split("\\.")[0];

		MdmsCriteriaReq mdmsCriteriaReq = MdmsCriteriaReq.builder().requestInfo(requestInfo)
				.mdmsCriteria(MdmsCriteria.builder().moduleDetails(moduleDetailList).tenantId(tenantId).build())
				.build();

		JSONArray jsonArray = getMdmsResponse(mdmsCriteriaReq).getMdmsRes().get("egf-master").get("financialYears");

		if (CollectionUtils.isEmpty(jsonArray))
			return null;
		else
			return new ObjectMapper().convertValue(jsonArray.get(0), FinancialYear.class);
	}

	private MdmsResponse getMdmsResponse(MdmsCriteriaReq mdmsCriteriaReq) {

		String url = applicationProperties.getMdmsServiceHost() + applicationProperties.getMdmsServiceBasePath();
		log.debug("the url for mdms : "+url+" the request : "+mdmsCriteriaReq);
		return restTemplate.postForObject(url, mdmsCriteriaReq, MdmsResponse.class);
	}

}
