package org.egov.web.indexer.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.web.indexer.adaptor.ComplaintAdapter;
import org.egov.web.indexer.contract.SevaRequest;
import org.egov.web.indexer.models.ComplaintIndex;
import org.egov.web.indexer.service.ElasticSearchIndexerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class IndexerListener {

	public static final String OBJECT_TYPE_COMPLAINT = "complaint";

	@Autowired
	private ElasticSearchIndexerService elasticSearchIndexerService;

	@Autowired
	private ComplaintAdapter complaintAdapter;

	/**
	 * A key/value pair to be received from Kafka. Format of Value map:
	 * {"RequestInfo":{},"ServiceRequest":{"serviceRequestId":"somecrn","status"
	 * :true,"statusNotes":"COMPLETED", "values": {"locationId":"172",
	 * "childLocationId":"176"}}}
	 */
	@KafkaListener(id = "${kafka.topics.egov.index.id}",
            topics = "${kafka.topics.egov.index.name}",
            group = "${kafka.topics.egov.index.group}")
	public void listen(ConsumerRecord<String, SevaRequest> record) {
		try {
			SevaRequest sevaReq = record.value();
			ComplaintIndex complaintIndex = complaintAdapter.index(sevaReq.getServiceRequest());
			elasticSearchIndexerService.indexObject(OBJECT_TYPE_COMPLAINT, complaintIndex.getCrn(), complaintIndex);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
