package org.egov.swm.persistence.queue.repository;

import org.egov.swm.web.requests.SanitationStaffScheduleRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class SanitationStaffScheduleQueueRepository {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${egov.swm.sanitationstaffschedule.save.topic}")
    private String saveTopic;

    @Value("${egov.swm.sanitationstaffschedule.update.topic}")
    private String updateTopic;

    @Value("${egov.swm.sanitationstaffschedule.indexer.topic}")
    private String indexerTopic;

    public SanitationStaffScheduleRequest save(final SanitationStaffScheduleRequest sanitationStaffScheduleRequest) {

        kafkaTemplate.send(saveTopic, sanitationStaffScheduleRequest);

        kafkaTemplate.send(indexerTopic, sanitationStaffScheduleRequest);

        return sanitationStaffScheduleRequest;

    }

    public SanitationStaffScheduleRequest update(final SanitationStaffScheduleRequest sanitationStaffScheduleRequest) {

        kafkaTemplate.send(updateTopic, sanitationStaffScheduleRequest);

        kafkaTemplate.send(indexerTopic, sanitationStaffScheduleRequest);

        return sanitationStaffScheduleRequest;

    }

}