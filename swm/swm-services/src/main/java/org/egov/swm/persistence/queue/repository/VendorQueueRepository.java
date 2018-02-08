package org.egov.swm.persistence.queue.repository;

import org.egov.swm.domain.model.Vendor;
import org.egov.swm.persistence.repository.DocumentJdbcRepository;
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

    @Autowired
    private DocumentJdbcRepository documentJdbcRepository;

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

        for (final Vendor v : vendorRequest.getVendors()) {

            servicedLocationsJdbcRepository.delete(v.getTenantId(), v.getVendorNo());

            servicesOfferedJdbcRepository.delete(v.getTenantId(), v.getVendorNo());

            documentJdbcRepository.delete(v.getTenantId(), v.getVendorNo());

        }

        kafkaTemplate.send(updateTopic, vendorRequest);

        kafkaTemplate.send(indexerTopic, vendorRequest);

        return vendorRequest;

    }

}