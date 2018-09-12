package org.egov.pncalculator.controller;

import org.egov.pncalculator.models.PostFilterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import net.minidev.json.JSONArray;

@Controller
@RequestMapping("/propertytax")
public class CalculatorController {
	
	@Autowired
	private RestTemplate restTemplate;


	@PostMapping("/_recalculate")
	public ResponseEntity<String> getTaxEstimation(@RequestBody PostFilterRequest req) {
		
		
		DocumentContext resDc = JsonPath.parse(req.getResponse());
		DocumentContext reqDc = JsonPath.parse(req.getRequest());
		
		
		String propertyId = resDc.read("$.Properties.[0].propertyId");
		String tenantId = resDc.read("$.Properties.[0].tenantId");
		String assessmentNumber = resDc.read("$.Properties.[0].propertyDetails.[0].assessmentNumber");
		System.err.println(" the consumer code : " + propertyId+":"+assessmentNumber);
		
		reqDc.put("$.RequestInfo", "authToken", "5cbc415f-545c-45ad-8f22-5dac2ec90cd3");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> entity = new HttpEntity<String>(reqDc.jsonString() ,headers);

		String dmdRes = restTemplate.postForEntity(
				"https://egov-micro-dev.egovernments.org/billing-service/demand/_search?tenantId=" + tenantId
						+ "&consumerCode=" + propertyId + ":" + assessmentNumber + "&businessService=PT",
				entity, String.class).getBody();

		System.err.println(" the res : " + dmdRes);
		DocumentContext dmdResContext = JsonPath.parse(dmdRes);
		
		JSONArray array = dmdResContext.read("$.Demands[0].demandDetails");
		for (int i = 0; i < array.size(); i++) {

			String code = dmdResContext.read("$.Demands[0].demandDetails[" + i + "].taxHeadMasterCode");
			if (code.equalsIgnoreCase("PT_FIRE_CESS")) {
				dmdResContext.put("$.Demands[0].demandDetails[" + i + "]", "taxAmount", "299");
			}
		}
		dmdResContext.put("$", "RequestInfo", reqDc.read("$.RequestInfo"));
		HttpEntity<String> entity1 = new HttpEntity<String>(dmdResContext.jsonString(), headers);
		String dmdUpdateRes = restTemplate
				.postForEntity("https://egov-micro-dev.egovernments.org/billing-service/demand/_update", entity1,
						String.class)
				.getBody();
		System.err.println("demand update res : " + dmdUpdateRes);
		
		return new ResponseEntity<String>( resDc.jsonString(), HttpStatus.OK);
	}

}
