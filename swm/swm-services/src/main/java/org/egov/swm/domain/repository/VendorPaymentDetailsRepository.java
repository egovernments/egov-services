package org.egov.swm.domain.repository;

import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.VendorPaymentDetails;
import org.egov.swm.domain.model.VendorPaymentDetailsSearch;
import org.egov.swm.persistence.queue.repository.VendorPaymentDetailsQueueRepository;
import org.egov.swm.persistence.repository.DocumentJdbcRepository;
import org.egov.swm.persistence.repository.VendorPaymentDetailsJdbcRepository;
import org.egov.swm.web.requests.VendorPaymentDetailsRequest;
import org.springframework.stereotype.Service;

@Service
public class VendorPaymentDetailsRepository {

	private VendorPaymentDetailsQueueRepository vendorPaymentDetailsQueueRepository;

	private VendorPaymentDetailsJdbcRepository vendorPaymentDetailsJdbcRepository;

	private DocumentJdbcRepository documentJdbcRepository;

	public VendorPaymentDetailsRepository(VendorPaymentDetailsQueueRepository vendorPaymentDetailsQueueRepository,
			VendorPaymentDetailsJdbcRepository vendorPaymentDetailsJdbcRepository,
			DocumentJdbcRepository documentJdbcRepository) {

		this.vendorPaymentDetailsQueueRepository = vendorPaymentDetailsQueueRepository;
		this.vendorPaymentDetailsJdbcRepository = vendorPaymentDetailsJdbcRepository;
		this.documentJdbcRepository = documentJdbcRepository;
	}

	public VendorPaymentDetailsRequest create(VendorPaymentDetailsRequest vendorPaymentDetailsRequest) {
		return vendorPaymentDetailsQueueRepository.save(vendorPaymentDetailsRequest);
	}

	public VendorPaymentDetailsRequest update(VendorPaymentDetailsRequest vendorPaymentDetailsRequest) {

		for (VendorPaymentDetails details : vendorPaymentDetailsRequest.getVendorPaymentDetails()) {
			documentJdbcRepository.delete(details.getTenantId(), details.getPaymentNo());
		}
		return vendorPaymentDetailsQueueRepository.update(vendorPaymentDetailsRequest);
	}

	public Pagination<VendorPaymentDetails> search(VendorPaymentDetailsSearch vendorPaymentDetailsSearch) {
		return vendorPaymentDetailsJdbcRepository.search(vendorPaymentDetailsSearch);

	}
}
