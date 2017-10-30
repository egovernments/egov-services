package org.egov.works.estimate.domain.repository;

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
		return detailedEstimateJdbcRepository.search(detailedEstimateSearchContract);
	}

	
	
}
