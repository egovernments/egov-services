package org.egov.web.indexer.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.web.indexer.adaptor.ComplaintAdapter;
import org.egov.web.indexer.contract.SevaRequest;
import org.egov.web.indexer.models.ComplaintIndex;
import org.egov.web.indexer.models.RequestContext;
import org.egov.web.indexer.service.ElasticSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class IndexerListener {

    private static final String OBJECT_TYPE_COMPLAINT = "complaint";

    private ElasticSearchService elasticSearchService;
    private ComplaintAdapter complaintAdapter;

    @Autowired
    public IndexerListener(ElasticSearchService elasticSearchService,
                           ComplaintAdapter complaintAdapter) {
        this.elasticSearchService = elasticSearchService;
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
        SevaRequest sevaRequest = record.value();
        RequestContext.setId(sevaRequest.getCorrelationId());
        ComplaintIndex complaintIndex = complaintAdapter.adapt(sevaRequest.getServiceRequest());
        elasticSearchService.index(OBJECT_TYPE_COMPLAINT, complaintIndex.getCrn(), complaintIndex);
    }
}
