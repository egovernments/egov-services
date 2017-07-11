package org.egov.pgrrest.read.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.common.contract.request.RequestInfo;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DraftDeleteRequest {

    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;

    private List<Long> ids;

}
