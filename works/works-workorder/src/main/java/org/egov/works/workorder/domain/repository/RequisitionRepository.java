package org.egov.works.workorder.domain.repository;

import org.egov.works.commons.utils.CommonUtils;
import org.egov.works.workorder.web.contract.DetailedEstimateSearchContract;
import org.egov.works.workorder.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RequisitionRepository {

    // private final LogAwareRestTemplate restTemplate;

    // private final String advanceRequisitionUrl;

    @Autowired
    private CommonUtils commonUtils;

    // @Autowired
    // public RequisitionRepository(final LogAwareRestTemplate restTemplate,
    // @Value("${egov.services.egov_works_estimate.hostname}") final String worksEstimateHostname,
    // @Value("${egov.services.egov_works_estimate.searchpath}") final String advanceRequisitionUrl,
    // final CommonUtils commonUtils) {
    //
    // this.restTemplate = restTemplate;
    // this.commonUtils = commonUtils;
    // this.advanceRequisitionUrl = worksEstimateHostname + advanceRequisitionUrl;
    // }

    public String createAdvanceRequisition() {
        // RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
        // requestInfoWrapper.setRequestInfo(requestInfo);
        // StringBuilder url = new StringBuilder();
        // url.append(advanceRequisitionUrl).append(tenantId);
        //
        // return restTemplate.postForObject(url.toString(), requestInfoWrapper, String.class);

        return commonUtils.getUUID();
    }

}
