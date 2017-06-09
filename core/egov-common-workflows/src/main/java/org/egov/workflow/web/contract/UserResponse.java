package org.egov.workflow.web.contract;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class UserResponse {
    @JsonProperty("responseInfo")
    ResponseInfo responseInfo;

    @JsonProperty("user")
    List<User> users;
    
    public boolean isGrievanceOfficer(){
        if (users.size() > 0){
            Set<Role> userRoles = users.get(0).getRoles();
            return userRoles
                    .stream()
                    .anyMatch(role -> role.getName().equals("Grievance Officer"));
        }
        return false;
    }
}
