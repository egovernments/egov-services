package org.egov.pgrrest.read.web.contract;

import lombok.*;

import java.util.HashMap;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Draft {
    private Long id;

    private HashMap<String, Object> draft;

    public Draft(org.egov.pgrrest.read.domain.model.Draft draft) {
        this.id = draft.getId();
        this.draft = draft.getDraft();
    }
}
