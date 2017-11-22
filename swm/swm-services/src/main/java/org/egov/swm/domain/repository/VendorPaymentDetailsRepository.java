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

    private final VendorPaymentDetailsQueueRepository vendorPaymentDetailsQueueRepository;

    private final VendorPaymentDetailsJdbcRepository vendorPaymentDetailsJdbcRepository;

    private final DocumentJdbcRepository documentJdbcRepository;

    public VendorPaymentDetailsRepository(final VendorPaymentDetailsQueueRepository vendorPaymentDetailsQueueRepository,
            final VendorPaymentDetailsJdbcRepository vendorPaymentDetailsJdbcRepository,
            final DocumentJdbcRepository documentJdbcRepository) {

        this.vendorPaymentDetailsQueueRepository = vendorPaymentDetailsQueueRepository;
        this.vendorPaymentDetailsJdbcRepository = vendorPaymentDetailsJdbcRepository;
        this.documentJdbcRepository = documentJdbcRepository;
    }

    public VendorPaymentDetailsRequest create(final VendorPaymentDetailsRequest vendorPaymentDetailsRequest) {
        return vendorPaymentDetailsQueueRepository.save(vendorPaymentDetailsRequest);
    }

    public VendorPaymentDetailsRequest update(final VendorPaymentDetailsRequest vendorPaymentDetailsRequest) {

        for (final VendorPaymentDetails details : vendorPaymentDetailsRequest.getVendorPaymentDetails())
            documentJdbcRepository.delete(details.getTenantId(), details.getPaymentNo());
        return vendorPaymentDetailsQueueRepository.update(vendorPaymentDetailsRequest);
    }

    public Pagination<VendorPaymentDetails> search(final VendorPaymentDetailsSearch vendorPaymentDetailsSearch) {
        return vendorPaymentDetailsJdbcRepository.search(vendorPaymentDetailsSearch);

    }
}
