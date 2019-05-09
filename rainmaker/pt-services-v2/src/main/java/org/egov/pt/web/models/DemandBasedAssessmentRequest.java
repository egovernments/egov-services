package org.egov.pt.web.models;

import lombok.Data;
import org.egov.common.contract.request.RequestInfo;

import java.util.List;

@Data
public class DemandBasedAssessmentRequest {

    RequestInfo requestInfo;

    List<DemandBasedAssessment> demandBasedAssessments;


}
