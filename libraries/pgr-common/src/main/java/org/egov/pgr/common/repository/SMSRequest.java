package org.egov.pgr.common.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
class SMSRequest {
    private String mobileNumber;
    private String message;
}