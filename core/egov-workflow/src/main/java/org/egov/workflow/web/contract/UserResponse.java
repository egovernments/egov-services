package org.egov.workflow.web.contract;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.egov.common.contract.response.ResponseInfo;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class UserResponse {
    @JsonProperty("responseInfo")
    ResponseInfo responseInfo;

    @JsonProperty("user")
    List<User> user;

    public boolean isGrievanceOfficer(){
        if (user.size() > 0){
            Set<Role> userRoles = user.get(0).getRoles();
            return userRoles
                    .stream()
                    .anyMatch(role -> role.getName().equals("Grievance Officer"));
        }
        return false;
    }
}
