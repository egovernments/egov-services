package org.egov.user.domain.model;

import lombok.*;
import org.egov.user.domain.exception.InvalidUserSearchCriteriaException;
import org.egov.user.domain.model.enums.UserType;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class UserSearchCriteria {

    private List<Long> id;
    private List<String> uuid;
    private String userName;
    private String name;
    private String mobileNumber;
    private String aadhaarNumber;
    private String pan;
    private String emailId;
    private boolean fuzzyLogic;
    private Boolean active;
    private Integer offset;
    private Integer limit;
    private List<String> sort;
    private UserType type;
    private String tenantId;
    private List<String> roleCodes;

    public void validate() {
        if (CollectionUtils.isEmpty(uuid) && CollectionUtils.isEmpty(id) && isEmpty(tenantId)) {
            throw new InvalidUserSearchCriteriaException(this);
        }
    }
}
