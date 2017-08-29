package org.egov.citizen.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.egov.citizen.model.RequestInfoWrapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class AuthTokenController {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${egov.services.user.hostname}")
	private String userHostName;
	
	@PostMapping("/token")
	public ResponseEntity<?> getAuthToken(@RequestBody RequestInfoWrapper requestInfoWrapper){
		String url = userHostName+"/user/oauth/token?grant_type=password&scope=read&"
				+ "username=murali&password=12345678&tenantId=default";
		
		log.info("url:"+url);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Authorization", "Basic ZWdvdi11c2VyLWNsaWVudDplZ292LXVzZXItc2VjcmV0");

		HttpEntity<String> entity = new HttpEntity<String>(headers);
		ResponseEntity<String> res = restTemplate.postForEntity(url, entity, String.class);
		System.out.println("idRes:" + res.getBody());
		String authTokRes = res.getBody();
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<>();
		try {
			  map = objectMapper.readValue(authTokRes, new TypeReference<Map<String, Object>>(){});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<>(map, HttpStatus.OK);
	}
	
}
