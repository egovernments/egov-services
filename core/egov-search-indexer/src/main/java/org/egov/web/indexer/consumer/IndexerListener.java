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

    private ElasticSearchIndexerService elasticSearchIndexerService;
    private ComplaintAdapter complaintAdapter;

    @Autowired
    public IndexerListener(ElasticSearchIndexerService elasticSearchIndexerService,
                           ComplaintAdapter complaintAdapter) {
        this.elasticSearchIndexerService = elasticSearchIndexerService;
        this.complaintAdapter = complaintAdapter;
    }

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
        SevaRequest sevaReq = record.value();
        ComplaintIndex complaintIndex = complaintAdapter.index(sevaReq.getServiceRequest());
        elasticSearchIndexerService.index(OBJECT_TYPE_COMPLAINT, complaintIndex.getCrn(), complaintIndex);
    }
}
