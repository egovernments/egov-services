package org.egov.tl.masters.persistence.queue;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FeeMatrixQueueRepository {
	@Autowired
	private FeeMatrixProducer feeMatrixProducer;

	@Value("${egov.tradelicense.feematrix.validated}")
	private String validatedTopic;

	@Value("${egov.tradelicense.feematrix.create.validated.key}")
	private String createValidatedKey;

	@Value("${egov.tradelicense.feematrix.update.validated.key}")
	private String updateValidatedKey;
	
	@Value("${egov.tradelicense.feematrixdetail.delete.validated.key}")
	private String deleteValidatedKey;

	private String topicKey;

	public void add(Map<String, Object> topicMap) {

		for (Map.Entry<String, Object> entry : topicMap.entrySet()) {

			String key = entry.getKey();

			if (key.equalsIgnoreCase(createValidatedKey)) {
				topicKey = createValidatedKey;
				break;
			}
			if (key.equalsIgnoreCase(updateValidatedKey)) {
				topicKey = updateValidatedKey;
				break;
			}
			if(key.equalsIgnoreCase(deleteValidatedKey)){
				topicKey = deleteValidatedKey;
				break;
			}
		}

		feeMatrixProducer.sendMessage(validatedTopic, topicKey, topicMap);
	}
}
