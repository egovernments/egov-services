package org.egov.models;

import javax.validation.Valid;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TitleTransferRequest {

    private RequestInfo requestInfo;

    @Valid
    private TitleTransfer titleTransfer;

}
