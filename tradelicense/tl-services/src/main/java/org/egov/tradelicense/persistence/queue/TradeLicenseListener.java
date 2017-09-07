package org.egov.tradelicense.persistence.queue;

import java.util.Map;

import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.contract.TradeLicenseContract;
import org.egov.tl.commons.web.requests.TradeLicenseIndexerRequest;
import org.egov.tl.commons.web.requests.TradeLicenseRequest;
import org.egov.tradelicense.common.config.PropertiesManager;
import org.egov.tradelicense.domain.model.TradeLicense;
import org.egov.tradelicense.domain.repository.TradeLicenseESRepository;
import org.egov.tradelicense.domain.service.TradeLicenseService;
import org.egov.tradelicense.web.contract.DemandResponse;
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
	RestTemplate restTemplate;

	@Autowired
	TradeLicenseESRepository tradeLicenseESRepository;

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
			indexerRequest = tradeLicenseESRepository.getTradeLicenseIndexerRequest(request);
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
			indexerRequest = tradeLicenseESRepository.getTradeLicenseIndexerRequest(request);
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
			indexerRequest = tradeLicenseESRepository.getTradeLicenseIndexerRequest(request);
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
			indexerRequest = tradeLicenseESRepository.getTradeLicenseIndexerRequest(request);
			mastersMap.put("tradelicense-persisted", indexerRequest);
			tradeLicenseProducer.sendMessage(topic, key, mastersMap);
		}
		if (mastersMap.get(propertiesManager.getUpdateDemandBillTopicName()) != null) {
			DemandResponse consumerRecord = objectMapper.convertValue(
					mastersMap.get(propertiesManager.getUpdateDemandBillTopicName()), DemandResponse.class);
			tradeLicenseService
					.updateTradeLicenseAfterCollection(objectMapper.convertValue(consumerRecord, DemandResponse.class));
			RequestInfo requestInfo = tradeLicenseService
					.createRequestInfoFromResponseInfo(consumerRecord.getResponseInfo());
			TradeLicense tradeLicense = tradeLicenseService.searchByApplicationNumber(requestInfo,
					consumerRecord.getDemands().get(0).getConsumerCode());
			tradeLicenseService.update(tradeLicense, requestInfo);
		}
	}

}