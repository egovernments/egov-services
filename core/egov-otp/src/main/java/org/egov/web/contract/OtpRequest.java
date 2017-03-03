package org.egov.web.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OtpRequest {
    private RequestInfo requestInfo;
    private String reference;
}

