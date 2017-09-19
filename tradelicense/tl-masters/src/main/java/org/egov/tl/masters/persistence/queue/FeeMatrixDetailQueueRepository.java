package org.egov.tl.masters.persistence.queue;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FeeMatrixDetailQueueRepository {
	@Autowired
	private FeeMatrixDetailProducer feeMatrixDetailProducer;

	@Value("${egov.tradelicense.feematrixdetail.validated}")
	private String validatedTopic;

	@Value("${egov.tradelicense.feematrixdetail.delete.validated.key}")
	private String deleteValidatedKey;

	private String topicKey;

	public void add(Map<String, Object> topicMap) {

		for (Map.Entry<String, Object> entry : topicMap.entrySet()) {

			String key = entry.getKey();

			if (key.equalsIgnoreCase(deleteValidatedKey)) {
				topicKey = deleteValidatedKey;
				break;
			}
		}
		feeMatrixDetailProducer.sendMessage(validatedTopic, topicKey, topicMap);
	}
}