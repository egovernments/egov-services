package org.egov.swm.persistence.queue.repository;

import org.egov.swm.web.requests.VendorContractRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class VendorContractQueueRepository {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${egov.swm.vendorcontract.save.topic}")
    private String saveTopic;

    @Value("${egov.swm.vendorcontract.update.topic}")
    private String updateTopic;

    @Value("${egov.swm.vendorcontract.indexer.topic}")
    private String indexerTopic;

    public VendorContractRequest save(final VendorContractRequest vendorContractRequest) {

        kafkaTemplate.send(saveTopic, vendorContractRequest);

        kafkaTemplate.send(indexerTopic, vendorContractRequest);

        return vendorContractRequest;

    }

    public VendorContractRequest update(final VendorContractRequest vendorContractRequest) {

        kafkaTemplate.send(updateTopic, vendorContractRequest);

        kafkaTemplate.send(indexerTopic, vendorContractRequest);

        return vendorContractRequest;

    }

}