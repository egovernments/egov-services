package org.egov.demand.model;

import java.math.BigDecimal;

import org.egov.demand.model.enums.Purpose;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillAccountDetail {

	private String id;

	private String tenantId;

	private String billDetail;
	
	private String glcode;

	private Integer order;

	private String accountDescription;
	
	@Default
	private BigDecimal crAmountToBePaid = BigDecimal.ZERO;

	private BigDecimal creditAmount;

	@Default
	private BigDecimal debitAmount = BigDecimal.ZERO;

	private Boolean isActualDemand;

	private Purpose purpose;
}