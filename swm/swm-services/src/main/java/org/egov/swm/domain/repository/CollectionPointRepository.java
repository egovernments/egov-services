package org.egov.swm.domain.repository;

import org.egov.swm.domain.model.CollectionPoint;
import org.egov.swm.domain.model.CollectionPointSearch;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.persistence.queue.repository.CollectionPointQueueRepository;
import org.egov.swm.persistence.repository.CollectionPointJdbcRepository;
import org.egov.swm.web.requests.CollectionPointRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CollectionPointRepository {

    @Autowired
    private CollectionPointJdbcRepository collectionPointJdbcRepository;

    @Autowired
    private CollectionPointQueueRepository collectionPointQueueRepository;

    public CollectionPointRequest save(final CollectionPointRequest collectionPointRequest) {

        return collectionPointQueueRepository.save(collectionPointRequest);

    }

    public CollectionPointRequest update(final CollectionPointRequest collectionPointRequest) {

        return collectionPointQueueRepository.update(collectionPointRequest);

    }

    public Pagination<CollectionPoint> search(final CollectionPointSearch collectionPointSearch) {
        return collectionPointJdbcRepository.search(collectionPointSearch);

    }

    public Boolean uniqueCheck(final String tenantId, final String fieldName, final String fieldValue,
            final String uniqueFieldName,
            final String uniqueFieldValue) {

        return collectionPointJdbcRepository.uniqueCheck(tenantId, fieldName, fieldValue, uniqueFieldName,
                uniqueFieldValue);
    }

}