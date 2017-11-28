package org.egov.swm.persistence.queue.repository;

import org.egov.swm.domain.model.SanitationStaffTarget;
import org.egov.swm.persistence.repository.SanitationStaffTargetMapJdbcRepository;
import org.egov.swm.web.requests.SanitationStaffTargetRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class SanitationStaffTargetQueueRepository {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private SanitationStaffTargetMapJdbcRepository sanitationStaffTargetMapJdbcRepository;

    @Value("${egov.swm.sanitationstafftarget.save.topic}")
    private String saveTopic;

    @Value("${egov.swm.sanitationstafftarget.update.topic}")
    private String updateTopic;

    @Value("${egov.swm.sanitationstafftarget.indexer.topic}")
    private String indexerTopic;

    public SanitationStaffTargetRequest save(final SanitationStaffTargetRequest sanitationStaffTargetRequest) {

        kafkaTemplate.send(saveTopic, sanitationStaffTargetRequest);

        kafkaTemplate.send(indexerTopic, sanitationStaffTargetRequest);

        return sanitationStaffTargetRequest;

    }

    public SanitationStaffTargetRequest update(final SanitationStaffTargetRequest sanitationStaffTargetRequest) {

        for (final SanitationStaffTarget sst : sanitationStaffTargetRequest.getSanitationStaffTargets())
            sanitationStaffTargetMapJdbcRepository.delete(sst.getTenantId(), sst.getTargetNo());

        kafkaTemplate.send(updateTopic, sanitationStaffTargetRequest);

        kafkaTemplate.send(indexerTopic, sanitationStaffTargetRequest);

        return sanitationStaffTargetRequest;

    }

}