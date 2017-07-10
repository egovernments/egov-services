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
    private long id;

    public DraftCreateResponse(long id) {
        this.id = id;
    }
}
