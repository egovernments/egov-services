package org.egov.tradelicense.persistence.queue;

import java.util.Map;

import org.egov.tradelicense.common.config.PropertiesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TradeLicenseQueueRepository {

	@Autowired
	private TradeLicenseProducer tradeLicenseProducer;

	@Autowired
	PropertiesManager propertiesManager;

	private String key;
	private String topicKey;

	public void add(Map<String, Object> topicMap) {
		for (Map.Entry<String, Object> entry : topicMap.entrySet()) {

			key = entry.getKey();

			if (key.equalsIgnoreCase(propertiesManager.getCreateLegacyTradeValidated())) {
				tradeLicenseProducer.sendMessage(propertiesManager.getCreateLegacyTradeValidated(), "", topicMap);
			}
		}

	}

}
