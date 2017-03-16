package org.egov.user.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.egov.user.domain.exception.InvalidUserException;
import org.egov.user.domain.model.enums.BloodGroup;
import org.egov.user.domain.model.enums.Gender;
import org.egov.user.domain.model.enums.GuardianRelation;
import org.egov.user.domain.model.enums.UserType;

import java.util.*;

import static org.springframework.util.ObjectUtils.isEmpty;

@AllArgsConstructor
@Getter
@Builder
public class User {

    private Long id;
    private String tenantId;
    private String username;
    private String title;
    private String password;
    private String salutation;
    private String guardian;
    private GuardianRelation guardianRelation;
    private String name;
    private Gender gender;
    private String mobileNumber;
    private String emailId;
    private String altContactNumber;
    private String pan;
    private String aadhaarNumber;
    private List<Address> address = new ArrayList<>();
    private boolean active;
    private Set<Role> roles = new HashSet<>();
    private Date dob;
    private Date pwdExpiryDate = new Date();
    private String locale = "en_IN";
    private UserType type;
    private BloodGroup bloodGroup;
    private String identificationMark;
    private String signature;
    private String photo;
    private boolean accountLocked;
    private Date lastModifiedDate;
    private Date createdDate;
    private String otpReference;
    private Long createdBy;
    private Long lastModifiedBy;

    public void validate() {
        if (isUsernameAbsent() || isNameAbsent() || isGenderAbsent() || isMobileNumberAbsent()
                || isActiveIndicatorAbsent() || isTypeAbsent()) {
            throw new InvalidUserException(this);
        }
    }

    public boolean isTypeAbsent() {
        return isEmpty(type);
    }

    public boolean isActiveIndicatorAbsent() {
        return isEmpty(active);
    }

    public boolean isGenderAbsent() {
        return isEmpty(gender);
    }

    public boolean isMobileNumberAbsent() {
        return isEmpty(mobileNumber);
    }

    public boolean isNameAbsent() {
        return isEmpty(name);
    }

    public boolean isUsernameAbsent() {
        return isEmpty(username);
    }

}
