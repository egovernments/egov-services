package org.egov.pgrrest.read.web.contract;

import lombok.*;

import java.util.HashMap;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DraftUpdateRequest {
    private Long id;
    private HashMap<String, Object> draft;

    public org.egov.pgrrest.read.domain.model.DraftUpdateRequest toDomain() {
        return org.egov.pgrrest.read.domain.model.DraftUpdateRequest.builder().id(id).draft(draft).build();
    }
}
