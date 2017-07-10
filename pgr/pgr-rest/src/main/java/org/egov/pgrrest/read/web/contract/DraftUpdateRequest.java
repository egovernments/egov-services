package org.egov.pgrrest.read.web.contract;

import lombok.*;
import org.egov.pgrrest.read.domain.model.UpdateDraft;

import java.util.HashMap;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DraftUpdateRequest {
    private Long id;
    private HashMap<String, Object> draft;

    public UpdateDraft toDomain() {
        return UpdateDraft.builder().id(id).draft(draft).build();
    }
}
