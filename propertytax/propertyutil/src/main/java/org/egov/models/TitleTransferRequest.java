package org.egov.models;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TitleTransferRequest {

    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;

    @Valid
    private TitleTransfer titleTransfer;

}
