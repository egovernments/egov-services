package org.egov.swm.domain.repository;

import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.VendorContract;
import org.egov.swm.domain.model.VendorContractSearch;
import org.egov.swm.persistence.repository.VendorContractJdbcRepository;
import org.egov.swm.web.requests.VendorContractRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class VendorContractRepository {

	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;

	@Autowired
	private VendorContractJdbcRepository vendorContractJdbcRepository;

	@Value("${egov.swm.vendorcontract.save.topic}")
	private String saveTopic;

	@Value("${egov.swm.vendorcontract.update.topic}")
	private String updateTopic;

	@Value("${egov.swm.vendorcontract.indexer.topic}")
	private String indexerTopic;

	public VendorContractRequest save(VendorContractRequest vendorContractRequest) {

		kafkaTemplate.send(saveTopic, vendorContractRequest);

		kafkaTemplate.send(indexerTopic, vendorContractRequest.getVendorContracts());

		return vendorContractRequest;

	}

	public VendorContractRequest update(VendorContractRequest vendorContractRequest) {

		kafkaTemplate.send(updateTopic, vendorContractRequest);

		kafkaTemplate.send(indexerTopic, vendorContractRequest.getVendorContracts());

		return vendorContractRequest;

	}

	public Pagination<VendorContract> search(VendorContractSearch vendorContractSearch) {
		return vendorContractJdbcRepository.search(vendorContractSearch);

	}

}