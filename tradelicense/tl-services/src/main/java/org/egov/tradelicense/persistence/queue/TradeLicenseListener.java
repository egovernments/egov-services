package org.egov.tradelicense.persistence.queue;

import java.util.Map;

import org.egov.tl.commons.web.contract.TradeLicenseContract;
import org.egov.tl.commons.web.requests.TradeLicenseRequest;
import org.egov.tradelicense.common.config.PropertiesManager;
import org.egov.tradelicense.domain.model.TradeLicense;
import org.egov.tradelicense.domain.service.TradeLicenseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
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

	@KafkaListener(topics = { "#{propertiesManager.getTradeLicenseWorkFlowPopulatedTopic()}" })
	public void process(Map<String, Object> mastersMap) {

		String topic = propertiesManager.getTradeLicensePersistedTopic();
		String key = propertiesManager.getTradeLicensePersistedKey();

		if (mastersMap.get("tradelicense-legacy-create") != null) {


			TradeLicenseRequest request = objectMapper.convertValue(mastersMap.get("tradelicense-legacy-create"),
					TradeLicenseRequest.class);

			ModelMapper mapper = new ModelMapper();
			for (TradeLicenseContract tradeLicenseContract : request.getLicenses()) {
				TradeLicense domain = mapper.map(tradeLicenseContract, TradeLicense.class);
				tradeLicenseService.save(domain);
			}
			mastersMap.clear();
			mastersMap.put("tradelicense-persisted", request);
			tradeLicenseProducer.sendMessage(topic, key, mastersMap);
		}
		if (mastersMap.get("tradelicense-new-create") != null) {


			TradeLicenseRequest request = objectMapper.convertValue(mastersMap.get("tradelicense-new-create"),
					TradeLicenseRequest.class);

			ModelMapper mapper = new ModelMapper();
			for (TradeLicenseContract tradeLicenseContract : request.getLicenses()) {
				TradeLicense domain = mapper.map(tradeLicenseContract, TradeLicense.class);
				tradeLicenseService.save(domain);
			}
			mastersMap.clear();
			mastersMap.put("tradelicense-persisted", request);
			tradeLicenseProducer.sendMessage(topic, key, mastersMap);
		}
		if (mastersMap.get("tradelicense-legacy-update") != null) {


			TradeLicenseRequest request = objectMapper.convertValue(mastersMap.get("tradelicense-legacy-update"),
					TradeLicenseRequest.class);

			ModelMapper mapper = new ModelMapper();
			for (TradeLicenseContract tradeLicenseContract : request.getLicenses()) {
				TradeLicense domain = mapper.map(tradeLicenseContract, TradeLicense.class);
				tradeLicenseService.update(domain);
			}
			mastersMap.clear();
			mastersMap.put("tradelicense-persisted", request);
			tradeLicenseProducer.sendMessage(topic, key, mastersMap);
		}
		if (mastersMap.get("tradelicense-new-update") != null) {


			TradeLicenseRequest request = objectMapper.convertValue(mastersMap.get("tradelicense-new-update"),
					TradeLicenseRequest.class);

			ModelMapper mapper = new ModelMapper();
			for (TradeLicenseContract tradeLicenseContract : request.getLicenses()) {
				TradeLicense domain = mapper.map(tradeLicenseContract, TradeLicense.class);
				tradeLicenseService.update(domain);
			}
			mastersMap.clear();
			mastersMap.put("tradelicense-persisted", request);
			tradeLicenseProducer.sendMessage(topic, key, mastersMap);
		}
	}

}