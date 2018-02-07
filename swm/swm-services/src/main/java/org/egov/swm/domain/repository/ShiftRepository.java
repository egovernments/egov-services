package org.egov.swm.domain.repository;

import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.Shift;
import org.egov.swm.domain.model.ShiftSearch;
import org.egov.swm.persistence.queue.repository.ShiftQueueRepository;
import org.egov.swm.persistence.repository.ShiftJdbcRepository;
import org.egov.swm.web.requests.ShiftRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShiftRepository {

    @Autowired
    private ShiftJdbcRepository shiftJdbcRepository;

    @Autowired
    private ShiftQueueRepository shiftQueueRepository;

    public ShiftRequest save(final ShiftRequest shiftRequest) {

        return shiftQueueRepository.save(shiftRequest);

    }

    public ShiftRequest update(final ShiftRequest shiftRequest) {

        return shiftQueueRepository.update(shiftRequest);

    }

    public Pagination<Shift> search(final ShiftSearch shiftSearch) {
        return shiftJdbcRepository.search(shiftSearch);

    }

    public Boolean uniqueCheck(final String tenantId, final String fieldName, final String fieldValue,
            final String uniqueFieldName,
            final String uniqueFieldValue) {

        return shiftJdbcRepository.uniqueCheck(tenantId, fieldName, fieldValue, uniqueFieldName,
                uniqueFieldValue);
    }

}