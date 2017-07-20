package org.egov.collection.web.contract;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.egov.collection.model.AuditDetails;
import org.egov.collection.model.WorkflowDetails;

import com.fasterxml.jackson.annotation.JsonProperty;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@ToString
public class Receipt {

	private String tenantId;

	private String instrumentType;

	private String instrumentHeader;

	@NotNull
	@JsonProperty("Bill")
	private List<Bill> bill = new ArrayList<>();

	@JsonProperty("Bank")
	private Bank bank;

	@NotNull
	@JsonProperty("BankAccount")
	private BankAccount bankAccount;

	private AuditDetails auditDetails;

	transient private WorkflowDetails workflowDetails;

}
