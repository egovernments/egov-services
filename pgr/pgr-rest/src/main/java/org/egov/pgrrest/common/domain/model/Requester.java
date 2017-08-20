package org.egov.pgrrest.common.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import static org.springframework.util.StringUtils.isEmpty;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


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

    public boolean isAbsent() {
        return isFirstNameAbsent() || isMobileAbsent();
    }

    public boolean isMobileAbsent() {
        return isEmpty(mobile);
    }

    public boolean isFirstNameAbsent() {
        return isEmpty(firstName);
    }

    public boolean isValidEmailAddress() {
        if (isEmpty(email)) {
            return true;
        }
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1," +
            "3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern p = Pattern.compile(ePattern);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public boolean isMobileNumberValid(){
        String mobileNumberPattern = "^[0-9]{10}$";
        Pattern p = Pattern.compile(mobileNumberPattern);
        Matcher m = p.matcher(mobile);
        return m.matches();
    }

    public void maskMobileAndEmailDetails() {
        this.mobile = null;
        this.email = null;
    }
}
