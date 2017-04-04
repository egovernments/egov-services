package org.egov.access.web.contact;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.access.domain.model.ActionSearchCriteria;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ActionRequest {

    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;
    @JsonProperty("RoleIds")
    private List<Long> roleIds;
    private List<Long> featureIds;

    public ActionSearchCriteria toDomain() {
        return ActionSearchCriteria.builder().roleIds(roleIds).build();
    }
}
