package org.egov.lcms.notification.repository;

import java.net.URI;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.lcms.notification.config.PropertiesManager;
import org.egov.lcms.notification.model.AdvocateSearchCriteria;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class AdvocateRepository {

	@Autowired
	PropertiesManager propertiesManager;

	public String getAdvocateName(String tenantId, List<String> code, RequestInfo requestInfo) throws Exception {

		String advocateName = null;
		final RestTemplate restTemplate = new RestTemplate();
		String[] codeArr = new String[code.size()];
		codeArr = code.toArray(codeArr);
		
		final StringBuffer userUrl = new StringBuffer();
		userUrl.append(propertiesManager.getAdvocateHostName() + propertiesManager.getAdvocateBasepath());
		userUrl.append(propertiesManager.getAdvocateSearchpath());

		final URI uri = UriComponentsBuilder.fromHttpUrl(userUrl.toString()).build().encode().toUri();
		AdvocateSearchCriteria advocateSearchCriteria = new AdvocateSearchCriteria();
		advocateSearchCriteria.setTenantId(tenantId);
		advocateSearchCriteria.setCode(codeArr);

		log.info("Get Advocate url is " + uri + " Advocate search criteria object is : " + advocateSearchCriteria);

		try {

			String response = restTemplate.postForObject(uri, advocateSearchCriteria, String.class);
			JSONParser parser = new JSONParser();
			JSONObject advocateResponse = (JSONObject) parser.parse(response);
			JSONArray advocates = (JSONArray) advocateResponse.get("advocates");
			JSONObject advocateObj = (JSONObject) advocates.get(0);
			advocateName = advocateObj.get("name").toString();

		} catch (final HttpClientErrorException exception) {

			log.info("Error occured while fetching User Details!");
			exception.printStackTrace();
		}
		return advocateName;
	}
}
