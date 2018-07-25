package org.egov.pg.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class Receipt {

    private String tenantId;

    private String id;

    private String transactionId;

    @NotNull
    @JsonProperty("Bill")
    private List<Bill> bill = new ArrayList<>();

    private AuditDetails auditDetails;

    @NotNull
    private Instrument instrument;

    private OnlinePayment onlinePayment;

    private Long stateId;

    @JsonProperty("WorkflowDetails")
    private WorkflowDetailsRequest workflowDetails;
}
