package org.egov.pgr.read.persistence.repository;

import org.egov.pgr.common.contract.SevaRequest;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.tracer.model.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ComplaintMessageQueueRepository {

    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    private String topicName;

    @Autowired
    public ComplaintMessageQueueRepository(LogAwareKafkaTemplate<String, Object> kafkaTemplate,
                                           @Value("${outgoing.queue.name.suffix}") String topicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    public void save(SevaRequest sevaRequest) {
        //TODO: Setting of correlationId to be removed once API gateway does this
        sevaRequest.getRequestInfo().setCorrelationId(RequestContext.getId());
        kafkaTemplate.send(topicName, sevaRequest);
    }
}
