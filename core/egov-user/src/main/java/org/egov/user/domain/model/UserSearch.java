package org.egov.user.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.egov.user.domain.exception.InvalidUserSearchException;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class UserSearch {

    private List<Long> id;

    private String userName;

    private String name;

    private String mobileNumber;

    private String aadhaarNumber;

    private String pan;

    private String emailId;

    private boolean fuzzyLogic;

    public void validate() {
        if (allFieldsAreNotPresent()) {
            throw new InvalidUserSearchException(this);
        }
    }

    private boolean isNameNotPresent() {
        return StringUtils.isBlank(name);
    }

    private boolean allFieldsAreNotPresent() {
        return isNameNotPresent() && isIdNotPresent() && isUserNameNotPresent() && isMobileNumberNotPresent() &&
                isAadharNumberNotPresent() && isPanNotPresent() && isEmailIdNotPresent();
    }

    private boolean isIdNotPresent() {
        return id == null || id.size() == 0;
    }

    private boolean isUserNameNotPresent() {
        return StringUtils.isBlank(userName);
    }

    private boolean isMobileNumberNotPresent() {
        return StringUtils.isBlank(mobileNumber);
    }

    private boolean isAadharNumberNotPresent() {
        return StringUtils.isBlank(aadhaarNumber);
    }

    private boolean isPanNotPresent() {
        return StringUtils.isBlank(pan);
    }

    private boolean isEmailIdNotPresent() {
        return StringUtils.isBlank(emailId);
    }
}
