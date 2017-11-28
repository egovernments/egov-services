package org.egov.swm.persistence.queue.repository;

import org.egov.swm.domain.model.Vendor;
import org.egov.swm.persistence.repository.ServicedLocationsJdbcRepository;
import org.egov.swm.persistence.repository.ServicesOfferedJdbcRepository;
import org.egov.swm.web.requests.VendorRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class VendorQueueRepository {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private ServicedLocationsJdbcRepository servicedLocationsJdbcRepository;

    @Autowired
    private ServicesOfferedJdbcRepository servicesOfferedJdbcRepository;

    @Value("${egov.swm.vendor.save.topic}")
    private String saveTopic;

    @Value("${egov.swm.vendor.update.topic}")
    private String updateTopic;

    @Value("${egov.swm.vendor.indexer.topic}")
    private String indexerTopic;

    public VendorRequest save(final VendorRequest vendorRequest) {

        kafkaTemplate.send(saveTopic, vendorRequest);

        kafkaTemplate.send(indexerTopic, vendorRequest);

        return vendorRequest;

    }

    public VendorRequest update(final VendorRequest vendorRequest) {

        for (final Vendor cp : vendorRequest.getVendors()) {

            servicedLocationsJdbcRepository.delete(cp.getTenantId(), cp.getVendorNo());

            servicesOfferedJdbcRepository.delete(cp.getTenantId(), cp.getVendorNo());
        }

        kafkaTemplate.send(updateTopic, vendorRequest);

        kafkaTemplate.send(indexerTopic, vendorRequest);

        return vendorRequest;

    }

}