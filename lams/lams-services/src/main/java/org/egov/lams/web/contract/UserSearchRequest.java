package org.egov.lams.web.contract;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSearchRequest {

    @JsonProperty("requestInfo")
    private RequestInfo requestInfo;

    @JsonProperty("id")
    private List<Long> id;


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

    @JsonProperty("tenantId")
    private String tenantId;

    @JsonProperty("pageSize")
    private int pageSize = 500;

    @JsonProperty("pageNumber")
    private int pageNumber = 0;

    @JsonProperty("sort")
    private List<String> sort = Collections.singletonList("name");

}
