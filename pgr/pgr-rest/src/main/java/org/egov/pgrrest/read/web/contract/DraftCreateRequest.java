package org.egov.pgrrest.read.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.common.contract.request.RequestInfo;

import java.util.HashMap;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DraftCreateRequest {
    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;

    private String tenantId;

    private String serviceCode;

    private HashMap<String, Object> draft;

    public org.egov.pgrrest.read.domain.model.DraftCreateRequest toDomain() {
        return org.egov.pgrrest.read.domain.model.DraftCreateRequest.builder().serviceCode(serviceCode).tenantId(tenantId).userId(requestInfo.getUserInfo().getId()).draft(draft).build();
    }
}
