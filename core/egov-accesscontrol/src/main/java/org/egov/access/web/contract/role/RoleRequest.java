package org.egov.access.web.contract.role;

import java.util.List;

import org.egov.access.domain.model.Role;
import org.egov.common.contract.request.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RoleRequest {

    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;
    
    private List<Role> roles;
}
