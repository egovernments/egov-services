package org.egov.swm.persistence.queue.repository;

import org.egov.swm.domain.model.VendorContract;
import org.egov.swm.persistence.repository.VendorContractServicesOfferedJdbcRepository;
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

    @Autowired
    private VendorContractServicesOfferedJdbcRepository vendorContractServicesOfferedJdbcRepository;

    public VendorContractRequest save(final VendorContractRequest vendorContractRequest) {

        kafkaTemplate.send(saveTopic, vendorContractRequest);

        kafkaTemplate.send(indexerTopic, vendorContractRequest);

        return vendorContractRequest;

    }

    public VendorContractRequest update(final VendorContractRequest vendorContractRequest) {

        for (final VendorContract cp : vendorContractRequest.getVendorContracts())
            vendorContractServicesOfferedJdbcRepository.delete(cp.getTenantId(), cp.getContractNo());

        kafkaTemplate.send(updateTopic, vendorContractRequest);

        kafkaTemplate.send(indexerTopic, vendorContractRequest);

        return vendorContractRequest;

    }

}