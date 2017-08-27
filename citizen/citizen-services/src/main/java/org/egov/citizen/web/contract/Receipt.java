package org.egov.citizen.web.contract;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.egov.citizen.model.AuditDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


import com.fasterxml.jackson.annotation.JsonProperty;

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
	
	private Long stateId;
	
	@JsonProperty("WorkflowDetails")
	private WorkflowDetailsRequest workflowDetails;
}
