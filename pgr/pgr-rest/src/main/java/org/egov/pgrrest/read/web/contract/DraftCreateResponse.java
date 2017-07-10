package org.egov.pgrrest.read.web.contract;

import lombok.*;
import org.egov.common.contract.response.ResponseInfo;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DraftCreateResponse {
    private ResponseInfo responseInfo;

    private boolean successful;

    private Long id;

    public DraftCreateResponse(org.egov.pgrrest.read.domain.model.DraftCreateResponse draftCreateResponse) {
        this.id = draftCreateResponse.getId();
        this.successful = true;
    }
}
