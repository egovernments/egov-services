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
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
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
			RequestInfoWrapper requestInfo,String moduleName) throws Exception {
		MdmsResponse mdmsResponse = null;

		// Master Detail
		List<MasterDetail> masterDetails = new ArrayList<MasterDetail>();
		for (String key : masterCodeAndValue.keySet()) {
			MasterDetail masterDetail = new MasterDetail();

			masterDetail.setName(key);
			String[] codes = masterCodeAndValue.get(key).split(",");

			if (codes.length == 1) {
				masterDetail.setFilter("[?(@.code==\"" + masterCodeAndValue.get(key) + "\")]");
			} else if (codes.length > 1) {
				StringBuffer code = new StringBuffer();
				for (int i = 0; i <= codes.length - 1; i++) {
					if (i == 0) {
						code.append("[?(@.code==\"" + codes[i] + "\"");
					} else {
						if (i == codes.length - 1) {
							code.append("|| @.code==" + "\"" + codes[i] + "\")]");
						} else {
							code.append("|| @.code==" + "\"" + codes[i] + "\"");
						}
					}
				}
				masterDetail.setFilter(code.toString());
			}

			masterDetails.add(masterDetail);
		}

		ModuleDetail moduleDetail = new ModuleDetail();
		moduleDetail.setModuleName(moduleName);

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
			throw new CustomException("MDMS_SERVICE_SEARCH_ERROR", "Failed to get master data");
		}

		return mdmsResponse;
	}
	
	public String getCommaSepratedValues(String[] code) {

		if (code.length > 0) {
			StringBuilder nameBuilder = new StringBuilder();

			for (String n : code) {
				nameBuilder.append(n).append(",");
			}

			nameBuilder.deleteCharAt(nameBuilder.length() - 1);

			return nameBuilder.toString();
		} else {
			return "";
		}
	}
}
