package org.egov.swm.domain.repository;

import org.egov.swm.domain.model.RefillingPumpStation;
import org.egov.swm.web.requests.RefillingPumpStationRequest;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RefillingPumpStationRepository {

    private LogAwareKafkaTemplate kafkaTemplate;

    private String createTopic;

    private String indexTopic;

    public RefillingPumpStationRepository(LogAwareKafkaTemplate kafkaTemplate,
                                          @Value("${egov.swm.refillingpumpstation.save.topic}") String createTopic,
                                          @Value("${egov.swm.refillingpumpstation.indexer.topic}") String indexTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.createTopic = createTopic;
        this.indexTopic = indexTopic;
    }

    public RefillingPumpStationRequest save(RefillingPumpStationRequest refillingPumpStationRequest){

        kafkaTemplate.send(createTopic, refillingPumpStationRequest);

//        refillingPumpStationRequest.getRefillingPumpStations()
//                .forEach(this::pushToCreateAndIndexTopic);

        return refillingPumpStationRequest;
    }

    private void pushToCreateAndIndexTopic(RefillingPumpStation refillingPumpStation){

        kafkaTemplate.send(createTopic, refillingPumpStation);

        kafkaTemplate.send(indexTopic, refillingPumpStation);
    }

}
