package org.egov.works.estimate.domain.repository;

import java.util.List;

import org.egov.works.estimate.persistence.repository.AbstractEstimateJdbcRepository;
import org.egov.works.estimate.web.contract.AbstractEstimateSearchContract;
import org.egov.works.estimate.web.model.AbstractEstimate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AbstractEstimateRepository {
	@Autowired
	private AbstractEstimateJdbcRepository abstractEstimateJdbcRepository;
	
	public String getNextSequence(final String sequenceName) {
		return abstractEstimateJdbcRepository.getSequence(sequenceName);
	}
	
	public List<AbstractEstimate> search(AbstractEstimateSearchContract abstractEstimateSearchContract) {
		return abstractEstimateJdbcRepository.search(abstractEstimateSearchContract);
	}
}
