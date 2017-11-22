package org.egov.swm.domain.repository;

import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.RefillingPumpStation;
import org.egov.swm.domain.model.RefillingPumpStationSearch;
import org.egov.swm.persistence.queue.repository.RefillingPumpStationQueueRepository;
import org.egov.swm.persistence.repository.RefillingPumpStationJdbcRepository;
import org.egov.swm.web.requests.RefillingPumpStationRequest;
import org.springframework.stereotype.Service;

@Service
public class RefillingPumpStationRepository {

    private final RefillingPumpStationQueueRepository refillingPumpStationQueueRepository;

    private final RefillingPumpStationJdbcRepository refillingPumpStationJdbcRepository;

    public RefillingPumpStationRepository(final RefillingPumpStationQueueRepository refillingPumpStationQueueRepository,
            final RefillingPumpStationJdbcRepository refillingPumpStationJdbcRepository) {
        this.refillingPumpStationQueueRepository = refillingPumpStationQueueRepository;
        this.refillingPumpStationJdbcRepository = refillingPumpStationJdbcRepository;
    }

    public RefillingPumpStationRequest save(final RefillingPumpStationRequest refillingPumpStationRequest) {

        return refillingPumpStationQueueRepository.save(refillingPumpStationRequest);
    }

    public RefillingPumpStationRequest update(final RefillingPumpStationRequest refillingPumpStationRequest) {

        return refillingPumpStationQueueRepository.update(refillingPumpStationRequest);
    }

    public Pagination<RefillingPumpStation> search(final RefillingPumpStationSearch refillingPumpStationSearch) {

        return refillingPumpStationJdbcRepository.search(refillingPumpStationSearch);
    }
}
