package org.egov.access.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.common.contract.request.RequestInfo;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RoleRequest {

    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;
}
