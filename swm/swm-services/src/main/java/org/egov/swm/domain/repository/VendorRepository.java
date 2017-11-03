package org.egov.swm.domain.repository;

import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.Vendor;
import org.egov.swm.domain.model.VendorSearch;
import org.egov.swm.persistence.repository.ServicedLocationsJdbcRepository;
import org.egov.swm.persistence.repository.ServicesOfferedJdbcRepository;
import org.egov.swm.persistence.repository.VendorJdbcRepository;
import org.egov.swm.web.requests.VendorRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class VendorRepository {

	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;

	@Autowired
	private VendorJdbcRepository vendorJdbcRepository;

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

	public VendorRequest save(VendorRequest vendorRequest) {

		kafkaTemplate.send(saveTopic, vendorRequest);

		kafkaTemplate.send(indexerTopic, vendorRequest.getVendors());

		return vendorRequest;

	}

	public VendorRequest update(VendorRequest vendorRequest) {

		for (Vendor cp : vendorRequest.getVendors()) {

			servicedLocationsJdbcRepository.delete(cp.getVendorNo());

			servicesOfferedJdbcRepository.delete(cp.getVendorNo());
		}

		kafkaTemplate.send(updateTopic, vendorRequest);

		kafkaTemplate.send(indexerTopic, vendorRequest.getVendors());

		return vendorRequest;

	}

	public Pagination<Vendor> search(VendorSearch vendorSearch) {
		return vendorJdbcRepository.search(vendorSearch);

	}

}