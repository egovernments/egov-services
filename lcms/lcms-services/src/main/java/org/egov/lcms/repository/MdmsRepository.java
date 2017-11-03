package org.egov.lcms.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.egov.lcms.config.PropertiesManager;
import org.egov.lcms.models.RequestInfoWrapper;
import org.egov.mdms.model.MasterDetail;
import org.egov.mdms.model.MdmsCriteria;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.mdms.model.MdmsResponse;
import org.egov.mdms.model.ModuleDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Prasad
 *
 */
@Repository
@Slf4j
public class MdmsRepository {

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	PropertiesManager propertiesManager;
	
	@Autowired
	ObjectMapper objectMapper;

	public MdmsResponse getMasterData(String tenantId, Map<String, String> masterCodeAndValue,
			RequestInfoWrapper requestInfo) throws Exception {
		MdmsResponse mdmsResponse = null;

		// Master Detail
		List<MasterDetail> masterDetails = new ArrayList<MasterDetail>();
		for (String key : masterCodeAndValue.keySet()) {
			MasterDetail masterDetail = new MasterDetail();

			masterDetail.setName(key);
			masterDetail.setFilter("[?(@.code==\"" + masterCodeAndValue.get(key) + "\")]");
			masterDetails.add(masterDetail);
		}

		ModuleDetail moduleDetail = new ModuleDetail();
		moduleDetail.setModuleName("lcms");

		moduleDetail.setMasterDetails(masterDetails);

		MdmsCriteria mdmsCriteria = new MdmsCriteria();
		List<ModuleDetail> moduleDetails = new ArrayList<ModuleDetail>();
		moduleDetails.add(moduleDetail);
		mdmsCriteria.setTenantId(tenantId);
		mdmsCriteria.setModuleDetails(moduleDetails);

		MdmsCriteriaReq mdmsCriteriaReq = new MdmsCriteriaReq();
		mdmsCriteriaReq.setMdmsCriteria(mdmsCriteria);
		mdmsCriteriaReq.setRequestInfo(requestInfo.getRequestInfo());

		final StringBuffer commomServiceUrl = new StringBuffer();
		commomServiceUrl.append(propertiesManager.getMdmsBasePath());
		commomServiceUrl.append(propertiesManager.getMdmsSearhPath());


		try {
			mdmsResponse = restTemplate.postForObject(commomServiceUrl.toString(), objectMapper.writeValueAsString(mdmsCriteriaReq),
					MdmsResponse.class);
			log.info("Get mdms response is :" + mdmsResponse);

		} catch (Exception exception) {
			log.info("Excpeption in getting the mdmsresponse "+exception.getMessage());
		}

		return mdmsResponse;
	}
}
