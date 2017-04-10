package org.egov.user.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ActionRequest {

    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;
    @JsonProperty("RoleCodes")
    private List<String> roleCodes;
    @JsonProperty("TenantId")
    private String tenantId;
}
