package org.egov.asset.contract;

import javax.validation.Valid;

import org.egov.asset.model.DepreciationReportCriteria;
import org.egov.common.contract.request.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepreciationReportRequest {

    @JsonProperty("RequestInfo")
    @Valid
    private RequestInfo requestInfo;

    @JsonProperty("DepreciationReport")
    @Valid
    private DepreciationReportCriteria depreciationReportCriteria;

}
