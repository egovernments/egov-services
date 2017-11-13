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

    private RefillingPumpStationQueueRepository refillingPumpStationQueueRepository;

    private RefillingPumpStationJdbcRepository refillingPumpStationJdbcRepository;

    public RefillingPumpStationRepository(RefillingPumpStationQueueRepository refillingPumpStationQueueRepository,
                                          RefillingPumpStationJdbcRepository refillingPumpStationJdbcRepository) {
        this.refillingPumpStationQueueRepository = refillingPumpStationQueueRepository;
        this.refillingPumpStationJdbcRepository = refillingPumpStationJdbcRepository;
    }

    public RefillingPumpStationRequest save(RefillingPumpStationRequest refillingPumpStationRequest){

        return refillingPumpStationQueueRepository.save(refillingPumpStationRequest);
    }

    public RefillingPumpStationRequest update(RefillingPumpStationRequest refillingPumpStationRequest){

        return refillingPumpStationQueueRepository.update(refillingPumpStationRequest);
    }

    public Pagination<RefillingPumpStation> search(RefillingPumpStationSearch refillingPumpStationSearch){

        return refillingPumpStationJdbcRepository.search(refillingPumpStationSearch);
    }
}
