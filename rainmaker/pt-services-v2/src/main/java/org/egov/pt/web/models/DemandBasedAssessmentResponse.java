package org.egov.pt.web.models;

import java.util.List;

import org.egov.common.contract.response.ResponseInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DemandBasedAssessmentResponse {

   @JsonProperty("ResponseInfo")
   ResponseInfo responseInfo;

   @JsonProperty("demandBasedAssessments")
   List<DemandBasedAssessment> demandBasedAssessments;
}
