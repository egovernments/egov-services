package org.egov.tl.masters.persistence.queue;

import java.util.Map;

import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.producers.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentTypeQueueRepository {

	@Autowired
	private Producer documentTypeProducer;

	@Autowired
	PropertiesManager propertiesManager;

	private String key;
	private String topicKey;

	public void add(Map<String, Object> topicMap) {
		for (Map.Entry<String, Object> entry : topicMap.entrySet()) {

			key = entry.getKey();

			if (key.equalsIgnoreCase(propertiesManager.getCreateDocumentTypeV2Validated())) {
				documentTypeProducer.send(propertiesManager.getCreateDocumentTypeV2Validated(),
						topicMap.get(propertiesManager.getCreateDocumentTypeV2Validated()));
			}

			if (key.equalsIgnoreCase(propertiesManager.getUpdateDocumentTypeV2Validated())) {
				documentTypeProducer.send(propertiesManager.getUpdateDocumentTypeV2Validated(),
						topicMap.get(propertiesManager.getUpdateDocumentTypeV2Validated()));
			}
		}

	}

}
