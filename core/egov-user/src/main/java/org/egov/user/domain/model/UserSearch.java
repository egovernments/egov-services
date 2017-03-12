package org.egov.user.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.JoinColumn;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class UserSearch {

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

}
