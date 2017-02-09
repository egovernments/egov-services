package org.egov.indexer.consumer;

import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.indexer.adaptor.ComplaintAdapter;
import org.egov.indexer.service.ElasticSearchIndexerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

public class IndexerListener {

	public static final String OBJECT_TYPE_COMPLAINT = "complaint";

	@Autowired
	private ElasticSearchIndexerService elasticSearchIndexerService;

	/**
	 * A key/value pair to be received from Kafka. Format of Value map:
	 * {"type":"complaint","content":{"id":"1015_35","crn":"03106-2016-EE",
	 * "complaintStatusName":"COMPLETED","complainantName":"C.Ravi"}}
	 */
	@KafkaListener(id = "${kafka.topics.egov.index.id}", topics = "${kafka.topics.egov.index.name}", group = "${kafka.topics.egov.index.group}")
	public void listen(ConsumerRecord<String, Map> record) {
		Map<String, Object> messageMap = record.value();
		String type = (String) messageMap.get("type");
		if (type != null && !type.isEmpty()) {
			if (type.equals(OBJECT_TYPE_COMPLAINT)) {
				try {
					elasticSearchIndexerService.indexObject(type,
							new ComplaintAdapter().index((Map) messageMap.get("content")));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
