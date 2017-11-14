package org.egov.swm.domain.repository;

import org.egov.swm.persistence.queue.repository.VendorPaymentDetailsQueueRepository;
import org.egov.swm.web.requests.VendorPaymentDetailsRequest;
import org.springframework.stereotype.Service;

@Service
public class VendorPaymentDetailsRepository {

    private VendorPaymentDetailsQueueRepository vendorPaymentDetailsQueueRepository;

    public VendorPaymentDetailsRepository(VendorPaymentDetailsQueueRepository vendorPaymentDetailsQueueRepository) {
        this.vendorPaymentDetailsQueueRepository = vendorPaymentDetailsQueueRepository;
    }

    public VendorPaymentDetailsRequest create(VendorPaymentDetailsRequest vendorPaymentDetailsRequest){
        return vendorPaymentDetailsQueueRepository.save(vendorPaymentDetailsRequest);
    }

    public VendorPaymentDetailsRequest update(VendorPaymentDetailsRequest vendorPaymentDetailsRequest){
        return vendorPaymentDetailsQueueRepository.update(vendorPaymentDetailsRequest);
    }
}
