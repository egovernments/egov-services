package org.egov.works.estimate.domain.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.egov.works.estimate.persistence.helper.DetailedEstimateHelper;
import org.egov.works.estimate.persistence.repository.AbstractEstimateDetailsJdbcRepository;
import org.egov.works.estimate.persistence.repository.DetailedEstimateJdbcRepository;
import org.egov.works.estimate.web.contract.AbstractEstimateDetailsSearchContract;
import org.egov.works.estimate.web.contract.DetailedEstimate;
import org.egov.works.estimate.web.contract.DetailedEstimateSearchContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DetailedEstimateRepository {

	@Autowired
	private DetailedEstimateJdbcRepository detailedEstimateJdbcRepository;

	@Autowired
	private AbstractEstimateDetailsJdbcRepository abstractEstimateDetailsJdbcRepository;

	public List<DetailedEstimate> search(DetailedEstimateSearchContract detailedEstimateSearchContract) {
		DetailedEstimate detailedEstimate;
		List<DetailedEstimate> detailedEstimates = new ArrayList<>();
		AbstractEstimateDetailsSearchContract abstractEstimateDetailsSearchContract;
		for (DetailedEstimateHelper estimate : detailedEstimateJdbcRepository
				.search(detailedEstimateSearchContract)) {
			detailedEstimate = estimate.toDomain(estimate);
			abstractEstimateDetailsSearchContract = new AbstractEstimateDetailsSearchContract();
			abstractEstimateDetailsSearchContract.setTenantId(estimate.getTenantId());
			abstractEstimateDetailsSearchContract
					.setAbstractEstimateIds(Arrays.asList(estimate.getAbstractEstimateDetail()));
			detailedEstimate.setAbstractEstimateDetail(
					abstractEstimateDetailsJdbcRepository.search(abstractEstimateDetailsSearchContract).get(0));
			detailedEstimates.add(detailedEstimate);
		}
		return detailedEstimates;
	}

}
