package org.egov.index.custom.service;

import java.util.HashMap;
import java.util.Map;

import org.egov.index.custom.models.pt.Property;
import org.egov.index.custom.models.pt.PropertyRequest;
import org.egov.index.custom.models.pt.PropertyResponse;
import org.egov.index.custom.producer.IndexerProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PTCustomService {
		
	@Value("${egov.pt.host}")
	private String ptHost;
	
	@Value("${egov.pt.search.endpoint}")
	private String ptSearchEndPoint;
	
	@Value("${kafka.topics.pt.update.index}")
	private String ptUpdateIndexTopic;
	
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private IndexerProducer producer;
	
	
	public void dataTransformForPTUpdate(PropertyRequest request) {
		for(Property property: request.getProperties()) {
			StringBuilder uri = new StringBuilder();
			uri.append(ptHost).append(ptSearchEndPoint).append("?tenantId=").append(property.getTenantId()).append("&ids=").append(property.getPropertyId());
			Map<String, Object> apiRequest = new HashMap<>();
			apiRequest.put("RequestInfo", request.getRequestInfo());
			try {
				PropertyResponse response = restTemplate.postForObject(uri.toString(), apiRequest, PropertyResponse.class);
				if(null != response) {
					if(!CollectionUtils.isEmpty(response.getProperties())) {
						property.getPropertyDetails().addAll(response.getProperties().get(0).getPropertyDetails()); //for search on propertyId, always just one property is returned.
					}else {
						log.info("Zero properties returned from the service!");
						log.info("Request: "+apiRequest);
						log.info("URI: "+uri);
						return;
					}
				}else {
					log.info("NULL returned from service!");
					log.info("Request: "+apiRequest);
					log.info("URI: "+uri);
					return;
				}
			}catch(Exception e) {
				log.error("Exception while fetching properties: ",e);
				log.info("Request: "+apiRequest);
				log.info("URI: "+uri);
				return;
			}
		}
		log.info("Pushing to consumer: "+request);
		producer.producer(ptUpdateIndexTopic, request);
		
	}
	
	
	
}