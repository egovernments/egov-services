package org.egov.access.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.access.domain.model.ActionSearchCriteria;
import org.egov.common.contract.request.RequestInfo;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ActionRequest {

    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;
    private List<String> roleCodes;
    private List<Long> featureIds;
    private String tenantId;

    public ActionSearchCriteria toDomain() {
        return ActionSearchCriteria.builder().tenantId(tenantId).roleCodes(roleCodes).build();
    }
}
