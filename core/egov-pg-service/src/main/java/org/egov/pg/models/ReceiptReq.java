package org.egov.pg.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.common.contract.request.RequestInfo;

import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
@ToString
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptReq {
    @JsonProperty("tenantId")
    private String tenantId = null;

    @NotNull
    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;

    @NotNull
    @JsonProperty("Receipt")
    private List<Receipt> receipt = null;

    @JsonProperty("WorkflowDetails")
    private WorkflowDetailsRequest workflowDetails;


}
