package org.egov.egf.bill.persistence.queue.repository;

import org.egov.egf.bill.web.requests.ChecklistRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ChecklistQueueRepository {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${egov.egf.bill.checklist.save.topic}")
    private String saveTopic;

    @Value("${egov.egf.bill.checklist.update.topic}")
    private String updateTopic;

    @Value("${egov.egf.bill.checklist.indexer.topic}")
    private String indexerTopic;

    public ChecklistRequest save(final ChecklistRequest checkListRequest) {

        kafkaTemplate.send(saveTopic, checkListRequest);

        kafkaTemplate.send(indexerTopic, checkListRequest);

        return checkListRequest;

    }

    public ChecklistRequest update(final ChecklistRequest checkListRequest) {

        kafkaTemplate.send(updateTopic, checkListRequest);

        kafkaTemplate.send(indexerTopic, checkListRequest);

        return checkListRequest;

    }
}
