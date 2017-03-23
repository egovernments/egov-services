package org.egov.demand.web.contract;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class DemandDetails {
	private Long id;
	private BigDecimal taxAmount;
	private BigDecimal collectionAmount;
	private BigDecimal rebateAmount;
	private String taxReason;
	private String taxPeriod;
	private Long glCode;
	private Integer isActualDemand;
}
