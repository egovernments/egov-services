package org.egov.swm.web.requests;

import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.swm.domain.model.PaginationContract;
import org.egov.swm.domain.model.SanitationStaffTarget;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(value = Include.NON_NULL)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public @Data class SanitationStaffTargetResponse {

    @JsonProperty("ResponseInfo")
    private ResponseInfo responseInfo;
    private List<SanitationStaffTarget> sanitationStaffTargets;
    private PaginationContract page;
}