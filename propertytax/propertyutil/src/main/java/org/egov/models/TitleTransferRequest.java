package org.egov.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TitleTransferRequest {

    private RequestInfo requestInfo;

    private TitleTransfer titleTransfer;

}
