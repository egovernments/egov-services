package org.egov.works.estimate.domain.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.tracer.model.CustomException;
import org.egov.works.commons.utils.CommonConstants;
import org.egov.works.estimate.config.PropertiesManager;
import org.egov.works.estimate.web.contract.AbstractEstimateDetails;
import org.egov.works.estimate.web.contract.EstimateAppropriation;
import org.egov.works.estimate.web.contract.EstimateAppropriationRequest;
import org.egov.works.estimate.web.contract.EstimateAppropriationResponse;
import org.egov.works.estimate.web.contract.RequestInfo;
import org.egov.works.estimate.config.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EstimateAppropriationService {

    @Autowired
    private PropertiesManager propertiesManager;

    @Autowired
    private RestTemplate restTemplate;

    public void setEstimateAppropriation(AbstractEstimateDetails abstractEstimateDetails,
            final RequestInfo requestInfo, Boolean isConsume) {
        checkBudgetAvailabe();
        EstimateAppropriationRequest estimateAppropriationRequest = new EstimateAppropriationRequest();
        EstimateAppropriation estimateAppropriation = new EstimateAppropriation();
        List<EstimateAppropriation> appropriations = new ArrayList<>();
        if (abstractEstimateDetails.getProjectCode() != null
                && abstractEstimateDetails.getProjectCode().getCode() != null)
            estimateAppropriation.setObjectNumber(abstractEstimateDetails.getProjectCode().getCode());
        estimateAppropriation.setObjectType(CommonConstants.ABSTRACT_ESTIMATE_BUSINESSKEY);
        estimateAppropriation.setTenantId(abstractEstimateDetails.getTenantId());
        estimateAppropriationRequest.setEstimateAppropriations(appropriations);
        estimateAppropriationRequest.setRequestInfo(requestInfo);
        String url = "";
        if (isConsume)
            url = propertiesManager.getWorksSeviceHostName() + propertiesManager.getCreateEstimateAppropriationURL();
        else
            url = propertiesManager.getWorksSeviceHostName() + propertiesManager.getUpdateEstimateAppropriationURL();

        restTemplate.postForObject(url, estimateAppropriationRequest, EstimateAppropriationResponse.class);
    }
    
    private void checkBudgetAvailabe() {
        Map<String, String> messages = new HashMap<>();
        // TODO : need to Check Budget control type and check budget available for given combination
        Boolean flag = Boolean.TRUE;

        String url = "";
        final RestTemplate restTemplate = new RestTemplate();
        RequestInfo requestInfo = new RequestInfo();
        restTemplate.postForObject(url, requestInfo, Object.class);

        if (!flag) {
            messages.put(Constants.KEY_ESTIMATEAPPROPRIATION_BUDGET_NOTAVAILABLE,
                    Constants.MESSAGE_ESTIMATEAPPROPRIATION_BUDGET_NOTAVAILABLE);
        }
        
        if (!messages.isEmpty())
            throw new CustomException(messages);
    }
}
