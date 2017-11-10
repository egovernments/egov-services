package org.egov.model;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.egov.model.enums.TransactionType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurrentValue {
	
	private Long id;
	
	@NotNull
	private String tenantId;
	
	@NotNull
	private Long assetId;
	
	private Long transactionDate;
	
	private BigDecimal currentAmount;
	
	private TransactionType assetTranType;

	private AuditDetails auditDetails;
}