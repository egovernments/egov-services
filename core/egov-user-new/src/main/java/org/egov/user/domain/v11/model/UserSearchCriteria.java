package org.egov.user.domain.v11.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class UserSearchCriteria {

    private List<Long> id;
    private String userName;
    private String name;
    private String mobileNumber;
    private String aadhaarNumber;
    private String pan;
    private String emailId;
    private Boolean active;
    private int pageSize;
    private int pageNumber;
    private String sort;
    private String type;
    private String tenantId;
    private List<String> roleCodes;
    private Long lastChangedSince;
    private Boolean includeDetails;
}
