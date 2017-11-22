package org.egov.swm.domain.repository;

import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.VendorContract;
import org.egov.swm.domain.model.VendorContractSearch;
import org.egov.swm.persistence.queue.repository.VendorContractQueueRepository;
import org.egov.swm.persistence.repository.VendorContractJdbcRepository;
import org.egov.swm.web.requests.VendorContractRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VendorContractRepository {

    @Autowired
    private VendorContractQueueRepository vendorContractQueueRepository;

    @Autowired
    private VendorContractJdbcRepository vendorContractJdbcRepository;

    public VendorContractRequest save(final VendorContractRequest vendorContractRequest) {

        return vendorContractQueueRepository.save(vendorContractRequest);

    }

    public VendorContractRequest update(final VendorContractRequest vendorContractRequest) {

        return vendorContractQueueRepository.update(vendorContractRequest);

    }

    public Pagination<VendorContract> search(final VendorContractSearch vendorContractSearch) {
        return vendorContractJdbcRepository.search(vendorContractSearch);

    }

}