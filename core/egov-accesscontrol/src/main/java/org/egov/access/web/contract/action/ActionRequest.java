package org.egov.access.web.contract.action;

import java.util.List;

import org.egov.access.domain.criteria.ActionSearchCriteria;
import org.egov.access.domain.model.Action;
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
public class ActionRequest {

    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;
    private List<String> roleCodes;
    private List<Long> featureIds;
    private String tenantId;
    
    private List<Action> actions;

    public ActionSearchCriteria toDomain() {
        return ActionSearchCriteria.builder().tenantId(tenantId).roleCodes(roleCodes).build();
    }
}
