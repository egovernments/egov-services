package org.egov.lams.model;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DemandDetails {
	private BigDecimal taxAmount;
	private BigDecimal collectionAmount;
	private BigDecimal rebateAmount;
	private String taxReason;
	private String taxPeriod;
	private Long glCode;
	private Integer isActualDemand;
}
