package org.egov.works.services.domain.repository;

import java.util.List;

import org.egov.works.services.web.contract.EstimateAppropriation;
import org.egov.works.services.web.contract.EstimateAppropriationSearchContract;
import org.egov.works.services.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class EstimateAppropriationRepository {

	@Autowired
	private EstimateAppropriationJdbcRepository estimateAppropriationJdbcRepository;
	
	public List<EstimateAppropriation> search(EstimateAppropriationSearchContract estimateAppropriationSearchContract) {
		return estimateAppropriationJdbcRepository.search(estimateAppropriationSearchContract);
	}
	
	public Boolean validateEstimateAppropriation(final EstimateAppropriation estimateAppropriation) {

		// TODO : need to Check Budget control type and check budget available
		// for given combination
		Boolean flag = Boolean.FALSE;
		String url = "";

		final RestTemplate restTemplate = new RestTemplate();

		RequestInfo requestInfo = new RequestInfo();

		restTemplate.postForObject(url, requestInfo, Object.class);

		return flag;

	}
	
}
