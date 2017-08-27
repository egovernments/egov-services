package org.egov.tradelicense.persistence.queue;

import java.util.HashMap;

import org.egov.tl.commons.web.requests.TradeLicenseRequest;
import org.egov.tradelicense.common.config.PropertiesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TradeLicenseQueueRepository {

	@Autowired
	private TradeLicenseProducer tradeLicenseProducer;

	@Autowired
	private PropertiesManager propertiesManager;

	public void add(TradeLicenseRequest request) {

		String topic = "", key = "";
		final HashMap<String, Object> tlMap = new HashMap<>();

		topic = propertiesManager.getTradeLicenseValidatedTopic();

		switch (request.getRequestInfo().getAction()) {

		case "legacy-create":

			key = propertiesManager.getLegacyTradeLicenseValidatedKey();

			tlMap.put("tradelicense-legacy-create", request);

			break;

		case "new-create":

			key = propertiesManager.getNewTradeLicenseValidatedKey();

			tlMap.put("tradelicense-new-create", request);

			break;

		case "legacy-update":

			key = propertiesManager.getLegacyTradeLicenseValidatedKey();

			tlMap.put("tradelicense-legacy-update", request);

			break;

		case "new-update":

			key = propertiesManager.getNewTradeLicenseValidatedKey();

			tlMap.put("tradelicense-new-update", request);

			break;
		}

		tradeLicenseProducer.sendMessage(topic, key, tlMap);

	}

}
