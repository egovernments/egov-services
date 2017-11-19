package org.egov.works.workorder.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.works.workorder.domain.repository.EstimateRepository;
import org.egov.works.workorder.web.contract.DetailedEstimate;
import org.egov.works.workorder.web.contract.DetailedEstimateResponse;
import org.egov.works.workorder.web.contract.DetailedEstimateSearchContract;
import org.egov.works.workorder.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstimateService {

	@Autowired
	private EstimateRepository estimateRepository;

	public DetailedEstimate getDetailedEstimate(final String detailedEstimate,final String tenantId,final RequestInfo requestInfo) {
		DetailedEstimateSearchContract detailedEstimateSearchContract = new DetailedEstimateSearchContract();
		final List<String> ids = new ArrayList<>();
		ids.add(detailedEstimate);
		detailedEstimateSearchContract.setIds(ids);
		List<String> statuses = new ArrayList<>();
		statuses.add("Technical Sanction");
		detailedEstimateSearchContract.setStatuses(statuses);
		detailedEstimateSearchContract.setTenantId(tenantId);
		final DetailedEstimateResponse detailedEstimateResponse = estimateRepository.getDetailedEstimateById(detailedEstimateSearchContract, 
				tenantId, requestInfo);
		return detailedEstimateResponse.getDetailedEstimates().get(0);
	}
}
