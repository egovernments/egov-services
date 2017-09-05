package org.egov.tradelicense.persistence.queue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.contract.TradeLicenseContract;
import org.egov.tl.commons.web.contract.TradeLicenseIndexerContract;
import org.egov.tl.commons.web.requests.RequestInfoWrapper;
import org.egov.tl.commons.web.requests.TradeLicenseIndexerRequest;
import org.egov.tl.commons.web.requests.TradeLicenseRequest;
import org.egov.tradelicense.common.config.PropertiesManager;
import org.egov.tradelicense.domain.model.TradeLicense;
import org.egov.tradelicense.domain.service.TradeLicenseService;
import org.egov.tradelicense.persistence.entity.TradeLicenseSearchEntity;
import org.egov.tradelicense.persistence.repository.TradeLicenseJdbcRepository;
import org.egov.tradelicense.web.contract.DemandResponse;
import org.egov.tradelicense.web.repository.TenantContractRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TradeLicenseListener {

	@Autowired
	ApplicationContext applicationContext;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	TradeLicenseProducer tradeLicenseProducer;

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	TradeLicenseService tradeLicenseService;

	@Autowired
	private TradeLicenseJdbcRepository tradeLicenseJdbcRepository;
	
	@Autowired
	private TenantContractRepository tenantWebContract;
	
	@Autowired
	RestTemplate restTemplate;

	@KafkaListener(topics = { "#{propertiesManager.getTradeLicenseWorkFlowPopulatedTopic()}" })
	public void process(Map<String, Object> mastersMap) {

		String topic = propertiesManager.getTradeLicensePersistedTopic();
		String key = propertiesManager.getTradeLicensePersistedKey();
		TradeLicenseIndexerRequest indexerRequest;
		if (mastersMap.get("tradelicense-legacy-create") != null) {

			TradeLicenseRequest request = objectMapper.convertValue(mastersMap.get("tradelicense-legacy-create"),
					TradeLicenseRequest.class);

			ModelMapper mapper = new ModelMapper();
			mapper.getConfiguration().setAmbiguityIgnored(true);
			for (TradeLicenseContract tradeLicenseContract : request.getLicenses()) {
				TradeLicense domain = mapper.map(tradeLicenseContract, TradeLicense.class);
				TradeLicense tradeLicense = tradeLicenseService.save(domain);
			}
			mastersMap.clear();
			indexerRequest = this.getTradeLicenseIndexerRequest(request);
			mastersMap.put("tradelicense-persisted", indexerRequest);
			tradeLicenseProducer.sendMessage(topic, key, mastersMap);
		}
		if (mastersMap.get("tradelicense-new-create") != null) {

			TradeLicenseRequest request = objectMapper.convertValue(mastersMap.get("tradelicense-new-create"),
					TradeLicenseRequest.class);

			ModelMapper mapper = new ModelMapper();
			mapper.getConfiguration().setAmbiguityIgnored(true);
			for (TradeLicenseContract tradeLicenseContract : request.getLicenses()) {
				TradeLicense domain = mapper.map(tradeLicenseContract, TradeLicense.class);
				tradeLicenseService.save(domain);
			}
			mastersMap.clear();
			indexerRequest = this.getTradeLicenseIndexerRequest(request);
			mastersMap.put("tradelicense-persisted", indexerRequest);
			tradeLicenseProducer.sendMessage(topic, key, mastersMap);
		}
		if (mastersMap.get("tradelicense-legacy-update") != null) {

			TradeLicenseRequest request = objectMapper.convertValue(mastersMap.get("tradelicense-legacy-update"),
					TradeLicenseRequest.class);

			ModelMapper mapper = new ModelMapper();
			mapper.getConfiguration().setAmbiguityIgnored(true);
			for (TradeLicenseContract tradeLicenseContract : request.getLicenses()) {
				TradeLicense domain = mapper.map(tradeLicenseContract, TradeLicense.class);
				tradeLicenseService.update(domain, request.getRequestInfo());
			}
			mastersMap.clear();
			indexerRequest = this.getTradeLicenseIndexerRequest(request);
			mastersMap.put("tradelicense-persisted", indexerRequest);
			tradeLicenseProducer.sendMessage(topic, key, mastersMap);
		}
		if (mastersMap.get("tradelicense-new-update") != null) {

			TradeLicenseRequest request = objectMapper.convertValue(mastersMap.get("tradelicense-new-update"),
					TradeLicenseRequest.class);

			ModelMapper mapper = new ModelMapper();
			mapper.getConfiguration().setAmbiguityIgnored(true);
			for (TradeLicenseContract tradeLicenseContract : request.getLicenses()) {
				TradeLicense domain = mapper.map(tradeLicenseContract, TradeLicense.class);
				tradeLicenseService.update(domain, request.getRequestInfo());
			}
			mastersMap.clear();
			indexerRequest = this.getTradeLicenseIndexerRequest(request);
			mastersMap.put("tradelicense-persisted", indexerRequest);
			tradeLicenseProducer.sendMessage(topic, key, mastersMap);
		}
		if (mastersMap.get(propertiesManager.getUpdateDemandBillTopicName()) != null) {
                        DemandResponse consumerRecord = objectMapper.convertValue(
                                mastersMap.get(propertiesManager.getUpdateDemandBillTopicName()),
                                DemandResponse.class);
                        tradeLicenseService
                                .updateTradeLicenseAfterCollection(objectMapper.convertValue(consumerRecord, DemandResponse.class));
                        RequestInfo requestInfo = tradeLicenseService.createRequestInfoFromResponseInfo(consumerRecord.getResponseInfo());
                        TradeLicense tradeLicense = tradeLicenseService.searchByApplicationNumber(requestInfo,
                                consumerRecord.getDemands().get(0).getConsumerCode());
                        tradeLicenseService.update(tradeLicense, requestInfo);
		}
	}

	public TradeLicenseIndexerRequest getTradeLicenseIndexerRequest(TradeLicenseRequest request) {
		TradeLicenseIndexerRequest indexerRequest = new TradeLicenseIndexerRequest();
		indexerRequest.setRequestInfo(request.getRequestInfo());

		List<TradeLicenseIndexerContract> tradeLicenseIndexer = new ArrayList<TradeLicenseIndexerContract>();
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(request.getRequestInfo());
		org.egov.models.City cityDetails = new org.egov.models.City();
		String tenantId ="";
		for (TradeLicenseContract tradeLicenseContract : request.getLicenses()) {

			TradeLicenseSearchEntity tradeLicenseSearchEntity = tradeLicenseJdbcRepository
					.searchById(request.getRequestInfo(), tradeLicenseContract.getId().longValue());

			
			ModelMapper mapper = new ModelMapper();
			mapper.getConfiguration().setAmbiguityIgnored(true);
			TradeLicenseIndexerContract tradeLicense = mapper.map(tradeLicenseSearchEntity.toDomain(), TradeLicenseIndexerContract.class);
			
			if( !tenantId.equalsIgnoreCase( tradeLicenseSearchEntity.getTenantId() )){
				tenantId = tradeLicenseSearchEntity.getTenantId();
				cityDetails = tenantWebContract.fetchTenantByCode(tenantId , requestInfoWrapper);
			}
			
			if( cityDetails != null ){
				tradeLicense.setCityName(cityDetails.getName());
				tradeLicense.setCityRegionName(cityDetails.getRegionName());
				tradeLicense.setCityGrade(cityDetails.getUlbGrade());
				tradeLicense.setCityDistrictName(cityDetails.getDistrictName());
				tradeLicense.setCityDistrictCode(cityDetails.getDistrictCode());
				tradeLicense.setCityCode(cityDetails.getCode()); 
			}
			
			
			tradeLicenseIndexer.add(tradeLicense);
		}
		indexerRequest.setLicenses(tradeLicenseIndexer);
		return indexerRequest;
	}
	

}