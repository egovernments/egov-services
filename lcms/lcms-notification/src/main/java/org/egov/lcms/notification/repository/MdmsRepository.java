package org.egov.lcms.notification.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.lcms.notification.config.PropertiesManager;
import org.egov.mdms.model.MasterDetail;
import org.egov.mdms.model.MdmsCriteria;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.mdms.model.MdmsResponse;
import org.egov.mdms.model.ModuleDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class MdmsRepository {

	@Autowired
	PropertiesManager propertiesManager;

	public String getDepartmentName(String tenantId, String departmentCode, RequestInfo requestInfo) throws Exception {

		final RestTemplate restTemplate = new RestTemplate();
		final ObjectMapper objectMapper = new ObjectMapper();

		MdmsResponse mdmsResponse = null;
		List<MasterDetail> masterDetails = new ArrayList<MasterDetail>();

		MasterDetail masterDetail = new MasterDetail();
		masterDetail.setName("Department");
		masterDetail.setFilter("[?(@.code==\"" + departmentCode + "\")]");
		masterDetails.add(masterDetail);

		ModuleDetail moduleDetail = new ModuleDetail();
		moduleDetail.setModuleName(propertiesManager.getCommonModuleName());
		moduleDetail.setMasterDetails(masterDetails);

		MdmsCriteria mdmsCriteria = new MdmsCriteria();
		List<ModuleDetail> moduleDetails = new ArrayList<ModuleDetail>();
		moduleDetails.add(moduleDetail);
		mdmsCriteria.setTenantId(tenantId);
		mdmsCriteria.setModuleDetails(moduleDetails);

		MdmsCriteriaReq mdmsCriteriaReq = new MdmsCriteriaReq();
		mdmsCriteriaReq.setMdmsCriteria(mdmsCriteria);
		mdmsCriteriaReq.setRequestInfo(requestInfo);

		final StringBuffer commomServiceUrl = new StringBuffer();
		commomServiceUrl.append(propertiesManager.getMdmsBasePath());
		commomServiceUrl.append(propertiesManager.getMdmsSearhPath());

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> entity = new HttpEntity<String>(objectMapper.writeValueAsString(mdmsCriteriaReq), headers);

		try {
			log.info("Mdms Request Object  is " + objectMapper.writeValueAsString(mdmsCriteriaReq));
			mdmsResponse = restTemplate.postForObject(commomServiceUrl.toString(), entity, MdmsResponse.class);
			log.info("Get mdms response is :" + mdmsResponse + " with body "
					+ objectMapper.writeValueAsString(mdmsCriteriaReq));
	
		} catch (Exception exception) {
			log.info("Excpeption in getting the mdmsresponse " + exception.getMessage());
			exception.printStackTrace();
		}

		return ((HashMap) mdmsResponse.getMdmsRes().get("common-masters").get("Department").get(0)).get("name").toString();
	}

}
