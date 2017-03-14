package org.egov.user.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.egov.user.domain.model.UserSearch;

import java.util.List;

@Getter
@Setter
public class UserSearchRequest {

    @JsonProperty("requestInfo")
    private RequestInfo requestInfo;

    @JsonProperty("id")
    private List<Long> id;

    @JsonProperty("userName")
    private String userName;

    @JsonProperty("name")
    private String name;

    @JsonProperty("mobileNumber")
    private String mobileNumber;

    @JsonProperty("aadhaarNumber")
    private String aadhaarNumber;

    @JsonProperty("pan")
    private String pan;

    @JsonProperty("emailId")
    private String emailId;

    @JsonProperty("fuzzyLogic")
    private boolean fuzzyLogic;

    @JsonProperty("active")
    private boolean active = true;

    @JsonProperty("tenantId")
    private String tenantId;

    public UserSearch toDomain() {
        return UserSearch.builder()
                .id(id)
                .userName(userName)
                .name(name)
                .mobileNumber(mobileNumber)
                .aadhaarNumber(aadhaarNumber)
                .pan(pan)
                .emailId(emailId)
                .fuzzyLogic(fuzzyLogic)
                .active(active)
                .build();
    }
}
