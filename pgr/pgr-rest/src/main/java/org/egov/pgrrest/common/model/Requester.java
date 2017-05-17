package org.egov.pgrrest.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import static org.springframework.util.StringUtils.isEmpty;


//A person who made a service request
@Getter
@AllArgsConstructor
@Builder
public class Requester {
    private String firstName;
    private String mobile;
    private String email;
    private String address;
    private String userId;
    private String tenantId;

    public boolean isAbsent() {
        return isFirstNameAbsent() || isMobileAbsent();
    }

    public boolean isMobileAbsent() {
        return isEmpty(mobile);
    }

    public boolean isFirstNameAbsent() {
        return isEmpty(firstName);
    }

}
