package org.egov.service;

import java.util.LinkedHashMap;

import org.egov.utils.JsonPathConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.jayway.jsonpath.DocumentContext;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SignOutService {

	@Autowired
	private RestTemplate restTemplate;

	public void callFinanceForSignOut(DocumentContext documentContext) {
		String response = null;
		try {
			String accessToken = documentContext.read(JsonPathConstant.signOutAccessToken);
			documentContext = documentContext.delete(JsonPathConstant.userInfo);
			documentContext = documentContext.put(JsonPathConstant.requestInfo, "authToken", accessToken);
			LinkedHashMap<String, Object> requestInfoWarpperMap = documentContext.read(JsonPathConstant.request);
			
			response = restTemplate.postForObject("https://jalandhar-dev.egovernments.org/services/EGF/rest/ClearToken",
					requestInfoWarpperMap, String.class);
			log.info("SignOutService response :"+response);
		} catch (HttpClientErrorException ex) {
			log.error(ex.getResponseBodyAsString());
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
