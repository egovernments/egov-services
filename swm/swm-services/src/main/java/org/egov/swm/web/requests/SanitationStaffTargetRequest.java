package org.egov.swm.web.requests;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.domain.model.SanitationStaffTarget;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

public @Data class SanitationStaffTargetRequest {

    @Valid
    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo = new RequestInfo();
    @Valid
    @NotNull
    @Size(min = 1)
    private List<SanitationStaffTarget> sanitationStaffTargets = new ArrayList<>();
}