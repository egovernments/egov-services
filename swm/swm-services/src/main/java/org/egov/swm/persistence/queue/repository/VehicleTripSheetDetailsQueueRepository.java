package org.egov.swm.persistence.queue.repository;

import org.egov.swm.web.requests.VehicleTripSheetDetailsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class VehicleTripSheetDetailsQueueRepository {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${egov.swm.vehicletripsheetdetails.save.topic}")
    private String saveTopic;

    @Value("${egov.swm.vehicletripsheetdetails.update.topic}")
    private String updateTopic;

    @Value("${egov.swm.vehicletripsheetdetails.indexer.topic}")
    private String indexerTopic;

    public VehicleTripSheetDetailsRequest save(final VehicleTripSheetDetailsRequest vehicleTripSheetDetailsRequest) {

        kafkaTemplate.send(saveTopic, vehicleTripSheetDetailsRequest);

        kafkaTemplate.send(indexerTopic, vehicleTripSheetDetailsRequest);

        return vehicleTripSheetDetailsRequest;

    }

    public VehicleTripSheetDetailsRequest update(final VehicleTripSheetDetailsRequest vehicleTripSheetDetailsRequest) {

        kafkaTemplate.send(updateTopic, vehicleTripSheetDetailsRequest);

        kafkaTemplate.send(indexerTopic, vehicleTripSheetDetailsRequest);

        return vehicleTripSheetDetailsRequest;

    }

}