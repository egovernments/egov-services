package org.egov.pgr.read.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
public class RequestInfo {

    @NotNull
    @Valid
    @JsonProperty("RequestInfo")
    private org.egov.pgr.common.contract.RequestInfo requestInfo;

}