package org.egov.works.workorder.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.works.workorder.domain.repository.EstimateRepository;
import org.egov.works.workorder.web.contract.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstimateService {

    @Autowired
    private EstimateRepository estimateRepository;

    public DetailedEstimateResponse getDetailedEstimate(final String id, final String tenantId,
            final RequestInfo requestInfo) {
        DetailedEstimateSearchContract detailedEstimateSearchContract = new DetailedEstimateSearchContract();
        final List<String> ids = new ArrayList<>();
        ids.add(id);
        detailedEstimateSearchContract.setDetailedEstimateNumbers(ids);
        List<String> statuses = new ArrayList<>();
        statuses.add(DetailedEstimateStatus.TECHNICAL_SANCTIONED.toString());
        detailedEstimateSearchContract.setStatuses(statuses);
        detailedEstimateSearchContract.setTenantId(tenantId);
        final DetailedEstimateResponse detailedEstimateResponse = estimateRepository.getDetailedEstimateById(
                detailedEstimateSearchContract,
                tenantId, requestInfo);
        return detailedEstimateResponse;
    }
}
