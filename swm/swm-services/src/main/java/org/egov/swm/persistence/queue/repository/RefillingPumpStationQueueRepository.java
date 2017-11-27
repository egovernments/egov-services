package org.egov.swm.persistence.queue.repository;

import org.egov.swm.web.requests.RefillingPumpStationRequest;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RefillingPumpStationQueueRepository {

    private final LogAwareKafkaTemplate kafkaTemplate;

    private final String createTopic;

    private final String updateTopic;

    private final String indexTopic;

    public RefillingPumpStationQueueRepository(final LogAwareKafkaTemplate kafkaTemplate,
            @Value("${egov.swm.refillingpumpstation.save.topic}") final String createTopic,
            @Value("${egov.swm.refillingpumpstation.update.topic}") final String updateTopic,
            @Value("${egov.swm.refillingpumpstation.indexer.topic}") final String indexTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.createTopic = createTopic;
        this.updateTopic = updateTopic;
        this.indexTopic = indexTopic;
    }

    public RefillingPumpStationRequest save(final RefillingPumpStationRequest refillingPumpStationRequest) {

        kafkaTemplate.send(createTopic, refillingPumpStationRequest);

        kafkaTemplate.send(indexTopic, refillingPumpStationRequest);

        return refillingPumpStationRequest;
    }

    public RefillingPumpStationRequest update(final RefillingPumpStationRequest refillingPumpStationRequest) {

        kafkaTemplate.send(updateTopic, refillingPumpStationRequest);

        kafkaTemplate.send(indexTopic, refillingPumpStationRequest);

        return refillingPumpStationRequest;
    }
}
