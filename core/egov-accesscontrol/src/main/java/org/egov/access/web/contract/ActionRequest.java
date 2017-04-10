package org.egov.access.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.access.domain.model.ActionSearchCriteria;
import org.egov.common.contract.request.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ActionRequest {

    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;
    @JsonProperty("RoleCodes")
    private List<String> roleCodes;
    private List<Long> featureIds;

    @NotNull
    @JsonProperty("TenantId")
    private String tenantId;

    public ActionSearchCriteria toDomain() {
        return ActionSearchCriteria.builder().tenantId(tenantId).roleCodes(roleCodes).build();
    }
}
