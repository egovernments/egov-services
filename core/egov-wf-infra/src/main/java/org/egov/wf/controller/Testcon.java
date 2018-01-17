package org.egov.wf.controller;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class Testcon {
	
	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;
	
	@Autowired
	RestTemplate restTemplate;
	
	@PostMapping(value = "_create")
	public ResponseEntity<?> createService(HttpEntity<String> httpEntity) {
		
		String serviceReqJson = httpEntity.getBody();
		log.info("serviceReqJson:"+serviceReqJson);
		
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> body = null;
		try {
			body = objectMapper.readValue(serviceReqJson, Map.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		DocumentContext documentContext = JsonPath.parse(serviceReqJson);
		LinkedHashMap<Object, Object> linkedHashMap = documentContext.json();
		kafkaTemplate.send("start-land-wf",body);
		//kafkaTemplate.send("update-land-wf",body);
	//	String url = "http://egov-micro-dev.egovernments.org/egov-mdms-service/v1/_get?moduleName=ASSET&masterName=AssetCategory&tenantId=default&filter=%5B%3F(%20%40.assetCategoryType%3D%3D%27IMMOVABLE%27%20%26%26%20%40.isAssetAllow%3D%3Dfalse)%5D";
	//	String bodyy = "{}";
	//	DocumentContext documentContext = JsonPath.parse(bodyy);
	//	System.out.println("JsonPath.read(serviceReqJson, \"$.RequestInfo\"):"+JsonPath.read(serviceReqJson, "$.RequestInfo"));
	//	documentContext.put("$", "RequestInfo", JsonPath.read(serviceReqJson, "$.RequestInfo"));
	//	makeModuleCall(url, documentContext.jsonString());
		return new ResponseEntity<>("done", HttpStatus.OK);
	}
	
	@GetMapping("_get")
	@ResponseBody
	public ResponseEntity<?> get(){
		return new ResponseEntity<>("done",HttpStatus.OK);
	}
	
	public String makeModuleCall(String url, String body) {

		System.out.println(url);
		System.out.println(body);
		
		URI uri = URI.create(url);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		//restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
		
		HttpEntity<String> entity = new HttpEntity<String>(body, headers);
		ResponseEntity<String> res = null;
		try {
			res = restTemplate.postForEntity(uri, entity, String.class);
		} catch (HttpClientErrorException ex) {
			ex.printStackTrace();
			String excep = ex.getResponseBodyAsString();
			log.info("HttpClientErrorException:" + excep);
			// throw new ModuleServiceCallException(excep);
		} catch (Exception ex) {
			log.info("Exception:" + ex.getMessage());
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}

		if (res == null) {
			throw new CustomException("workflow.response.null", "Failed while making call to work flow");
		}
		System.out.println("res:" + res.getBody());

		return res.getBody();
	}

}
