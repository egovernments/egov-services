package org.egov.works.estimate.domain.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.works.estimate.persistence.repository.DetailedEstimateJdbcRepository;
import org.egov.works.estimate.web.contract.DetailedEstimateSearchContract;
import org.egov.works.estimate.web.model.DetailedEstimate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DetailedEstimateRepository {

	
	@Autowired
	private DetailedEstimateJdbcRepository detailedEstimateJdbcRepository;
	
	public List<DetailedEstimate> search(DetailedEstimateSearchContract detailedEstimateSearchContract) {
		List<DetailedEstimate> detailedEstimates = new ArrayList<>();
		for(org.egov.works.estimate.web.contract.DetailedEstimate estimate : detailedEstimateJdbcRepository.search(detailedEstimateSearchContract)) {
			detailedEstimates.add(estimate.toDomain(estimate));
		}
		return detailedEstimates;
	}
	
}
