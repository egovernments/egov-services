package org.egov.citizen.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.citizen.config.ApplicationProperties;
import org.egov.citizen.model.IdGenerationReqWrapper;
import org.egov.citizen.model.IdRequest;
import org.egov.citizen.model.RequestInfoWrapper;
import org.egov.citizen.model.SearchDemand;
import org.egov.citizen.model.ServiceConfig;
import org.egov.citizen.model.ServiceReq;
import org.egov.citizen.model.Value;
import org.egov.citizen.producer.CitizenProducer;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CitizenService {

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private CitizenProducer citizenProducer; 
	
	@Autowired
	private ApplicationProperties applicationProperties;

	public List<Value> getQueryParameterList(List<ServiceConfig> list, String serviceCode, Object config) {

		List<Value> list1 = new ArrayList<Value>();
		for (ServiceConfig serviceConfig : list) {

			if (serviceConfig.getServiceCode().equals(serviceCode)) {
				SearchDemand searchDemand = serviceConfig.getSearchDemand();
				List<String> queryParamList = searchDemand.getQueryAppend();

				for (String queryParam : queryParamList) {

					String[] value = queryParam.split(":");
					Value val = new Value();
					val.setKey(value[0].trim());
					val.setValue(JsonPath.read(config, value[1]));
					list1.add(val);
				}
			}
		}
		return list1;
	}

	public RequestInfoWrapper getRequestInfo(Object config) {

		RequestInfo requestInfo = new RequestInfo();

		RequestInfoWrapper infoWrapper = new RequestInfoWrapper();

		requestInfo.setAction(JsonPath.read(config, "$.requestInfo.action"));
		requestInfo.setApiId(JsonPath.read(config, "$.requestInfo.apiId"));
		requestInfo.setAuthToken(JsonPath.read(config, "$.requestInfo.authToken"));
		requestInfo.setVer(JsonPath.read(config, "$.requestInfo.ver"));
		requestInfo.setTs(Long.valueOf(JsonPath.read(config, "$.requestInfo.ts").toString()));
		requestInfo.setDid(JsonPath.read(config, "$.requestInfo.did"));
		requestInfo.setMsgId(JsonPath.read(config, "$.requestInfo.msgId"));
		requestInfo.setKey(JsonPath.read(config, "$.requestInfo.key"));

		infoWrapper.setRequestInfo(requestInfo);

		return infoWrapper;
	}

	public String generateSequenceNumber(SearchDemand searchDemand, Object config) {

		IdRequest idRequest = searchDemand.getGenerateId().getRequest().getIdRequests().get(0);
		System.out.println("Expression: "+idRequest.getTenantId());
		Object tenantId = JsonPath.read(config, idRequest.getTenantId());
		idRequest.setTenantId(tenantId.toString());

		List<IdRequest> list = new ArrayList<IdRequest>();
		list.add(idRequest);
		IdGenerationReqWrapper reqWrapper = new IdGenerationReqWrapper();
		reqWrapper.setRequestInfo(getRequestInfo(config).getRequestInfo());
		reqWrapper.setIdRequests(list);

		Object response = new RestTemplate().postForObject(searchDemand.getGenerateId().getUrl(), reqWrapper,
				Object.class);

		Object generateId = JsonPath.read(response, searchDemand.getGenerateId().getResult());

		System.out.println("obje:  " + generateId);
		return generateId.toString();
	}
	
	public String getUrl(String url,List<Value> queryParamList){
		
		url = url + "?";
		for (Value val : queryParamList) {

			String url1 = val.getKey() + "=" + val.getValue() + "&";
			url = url + url1;
		}
		url = url.substring(0, url.length() - 1);
		
		return url;
	}
	
	public Object getResponse(String url,Object config,List<String> results){
		
		if (url != "") {
			Object response = restTemplate.postForObject(url,getRequestInfo(config), Object.class);
			
			int length = results.size();
			
			while(length !=0)
				
				for(int i=0;i<results.size();i++){
					
					
				}
			
			if (results != null && results.size() == 2) {
				Object responseObj = JsonPath.read(response, results.get(0));
				Object responseObj1 = JsonPath.read(responseObj, "$..Demands[0]");
				if (responseObj1 != "") {
					Object res = JsonPath.read(response, results.get(1));
					return res;
				}
			}
		}
	   return "";	
	}

	public void sendMessageToKafka(ServiceReq servcieReq){
		
		try {
			citizenProducer.producer(applicationProperties.getCreateServiceTopic(),
					applicationProperties.getCreateServiceTopicKey(), servcieReq);
		} catch (Exception e) {
			log.debug("service createAsync:" + e);
			throw new RuntimeException(e);
		}
		}
		
	}
