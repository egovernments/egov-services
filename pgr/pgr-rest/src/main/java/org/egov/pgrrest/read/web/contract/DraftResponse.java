package org.egov.pgrrest.read.web.contract;

import lombok.*;
import org.egov.common.contract.response.ResponseInfo;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DraftResponse {
    private ResponseInfo responseInfo;

    private boolean successful;
}
