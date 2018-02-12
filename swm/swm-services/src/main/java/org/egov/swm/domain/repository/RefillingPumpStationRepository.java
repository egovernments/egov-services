package org.egov.swm.domain.repository;

import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.RefillingPumpStation;
import org.egov.swm.domain.model.RefillingPumpStationSearch;
import org.egov.swm.persistence.queue.repository.RefillingPumpStationQueueRepository;
import org.egov.swm.persistence.repository.PumpStationFuelTypesJdbcRepository;
import org.egov.swm.persistence.repository.RefillingPumpStationJdbcRepository;
import org.egov.swm.web.requests.RefillingPumpStationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RefillingPumpStationRepository {

    @Autowired
    private RefillingPumpStationQueueRepository refillingPumpStationQueueRepository;

    @Autowired
    private RefillingPumpStationJdbcRepository refillingPumpStationJdbcRepository;

    @Autowired
    private PumpStationFuelTypesJdbcRepository pumpStationFuelTypesJdbcRepository;

    public RefillingPumpStationRequest save(final RefillingPumpStationRequest refillingPumpStationRequest) {

        return refillingPumpStationQueueRepository.save(refillingPumpStationRequest);
    }

    public RefillingPumpStationRequest update(final RefillingPumpStationRequest refillingPumpStationRequest) {

        for (final RefillingPumpStation pumpStation : refillingPumpStationRequest.getRefillingPumpStations())
            pumpStationFuelTypesJdbcRepository.delete(pumpStation.getTenantId(), pumpStation.getCode());

        return refillingPumpStationQueueRepository.update(refillingPumpStationRequest);
    }

    public Pagination<RefillingPumpStation> search(final RefillingPumpStationSearch refillingPumpStationSearch) {

        return refillingPumpStationJdbcRepository.search(refillingPumpStationSearch);
    }
}
