package org.egov.works.workorder.domain.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.egov.works.commons.utils.CommonConstants;
import org.egov.works.workorder.domain.repository.EstimateRepository;
import org.egov.works.workorder.web.contract.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstimateService {

    @Autowired
    private EstimateRepository estimateRepository;

    public DetailedEstimateResponse getDetailedEstimate(final String estimateNumber, final String tenantId,
            final RequestInfo requestInfo) {
        DetailedEstimateSearchContract detailedEstimateSearchContract = new DetailedEstimateSearchContract();
        detailedEstimateSearchContract.setDetailedEstimateNumbers(Arrays.asList(estimateNumber));
        List<String> statuses = new ArrayList<>();
        statuses.add(CommonConstants.STATUS_TECH_SANCTIONED);
        detailedEstimateSearchContract.setStatuses(statuses);
        detailedEstimateSearchContract.setTenantId(tenantId);
        final DetailedEstimateResponse detailedEstimateResponse = estimateRepository.getDetailedEstimateByEstimateNumber(
                detailedEstimateSearchContract,
                tenantId, requestInfo);
        return detailedEstimateResponse;
    }
}
