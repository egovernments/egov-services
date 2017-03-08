package org.egov.web.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Otp {
    private String mobileNumber;
    private String tenantId;
}
