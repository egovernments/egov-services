package org.egov.works.services.domain.repository;

import java.util.List;

import org.egov.works.services.web.contract.EstimateAppropriation;
import org.egov.works.services.web.contract.EstimateAppropriationSearchContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class EstimateAppropriationRepository {

    @Autowired
    private EstimateAppropriationJdbcRepository estimateAppropriationJdbcRepository;

    public List<EstimateAppropriation> search(EstimateAppropriationSearchContract estimateAppropriationSearchContract) {
        return estimateAppropriationJdbcRepository.search(estimateAppropriationSearchContract);
    }

}
