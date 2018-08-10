package org.egov.collection.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.collection.model.AuditDetails;
import org.egov.collection.model.Instrument;
import org.egov.collection.model.OnlinePayment;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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

    @NotNull
    private String tenantId;
	
	private String id;

    private String transactionId;

	@NotNull
    @Size(min = 1, max = 1)
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
