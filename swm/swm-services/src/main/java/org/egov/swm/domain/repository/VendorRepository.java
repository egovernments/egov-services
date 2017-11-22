package org.egov.swm.domain.repository;

import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.Vendor;
import org.egov.swm.domain.model.VendorSearch;
import org.egov.swm.persistence.queue.repository.VendorQueueRepository;
import org.egov.swm.persistence.repository.VendorJdbcRepository;
import org.egov.swm.web.requests.VendorRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VendorRepository {

    @Autowired
    private VendorJdbcRepository vendorJdbcRepository;

    @Autowired
    private VendorQueueRepository vendorQueueRepository;

    public VendorRequest save(final VendorRequest vendorRequest) {

        return vendorQueueRepository.save(vendorRequest);

    }

    public VendorRequest update(final VendorRequest vendorRequest) {

        return vendorQueueRepository.update(vendorRequest);

    }

    public Pagination<Vendor> search(final VendorSearch vendorSearch) {
        return vendorJdbcRepository.search(vendorSearch);

    }

    public Boolean uniqueCheck(final String tenantId, final String fieldName, final String fieldValue,
            final String uniqueFieldName,
            final String uniqueFieldValue) {

        return vendorJdbcRepository.uniqueCheck(tenantId, fieldName, fieldValue, uniqueFieldName, uniqueFieldValue);
    }

}