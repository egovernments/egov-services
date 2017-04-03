package org.egov.pgr.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import static org.springframework.util.StringUtils.isEmpty;


//A person who made a complaint
@Getter
@AllArgsConstructor
@Builder
public class Complainant {
    private String firstName;
    private String mobile;
    private String email;
    private String address;
    private String userId;

    public boolean isAbsent() {
        return isFirstNameAbsent() || isMobileAbsent();
    }

    public boolean isEmailAbsent() {
        return isEmpty(email);
    }

    public boolean isMobileAbsent() {
        return isEmpty(mobile);
    }

    public boolean isFirstNameAbsent() {
        return isEmpty(firstName);
    }

    public boolean isUserIdAbsent() {
        return isEmpty(userId);
    }


}
