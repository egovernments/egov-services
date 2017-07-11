package org.egov.pgrrest.read.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.common.contract.request.RequestInfo;
import org.egov.pgrrest.read.domain.model.NewDraft;

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

    public NewDraft toDomain() {
        return NewDraft.builder().serviceCode(serviceCode).tenantId(tenantId).userId(requestInfo.getUserInfo().getId()).draft(draft).build();
    }
}
