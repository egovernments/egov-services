package org.egov.swm.persistence.queue.repository;

import org.egov.swm.web.requests.ShiftRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ShiftQueueRepository {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${egov.swm.shift.save.topic}")
    private String saveTopic;

    @Value("${egov.swm.shift.update.topic}")
    private String updateTopic;

    @Value("${egov.swm.shift.indexer.topic}")
    private String indexerTopic;

    public ShiftRequest save(final ShiftRequest shiftRequest) {

        kafkaTemplate.send(saveTopic, shiftRequest);

        kafkaTemplate.send(indexerTopic, shiftRequest);

        return shiftRequest;

    }

    public ShiftRequest update(final ShiftRequest shiftRequest) {

        kafkaTemplate.send(updateTopic, shiftRequest);

        kafkaTemplate.send(indexerTopic, shiftRequest);

        return shiftRequest;

    }

}