package org.egov.works.estimate.domain.service;

import org.egov.works.estimate.domain.repository.AbstractEstimateRepository;
import org.egov.works.estimate.web.contract.AbstractEstimateRequest;
import org.egov.works.estimate.web.contract.AbstractEstimateResponse;
import org.egov.works.estimate.web.contract.AbstractEstimateSearchContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AbstractEstimateService {

	@Autowired
	private AbstractEstimateRepository abstractEstimateRepository;

	public AbstractEstimateResponse create(AbstractEstimateRequest abstractEstimateRequest) {
		return abstractEstimateRepository.create(abstractEstimateRequest);
	}

	public AbstractEstimateResponse update(AbstractEstimateRequest abstractEstimateRequest) {
		return abstractEstimateRepository.update(abstractEstimateRequest);
	}

	public AbstractEstimateResponse search(AbstractEstimateSearchContract abstractEstimateSearchContract) {
		return abstractEstimateRepository.search(abstractEstimateSearchContract);
	}
}
