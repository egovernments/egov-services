package org.egov.lams.contract;

import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DemandDetailsEs {

	private BigDecimal taxAmount;
	private BigDecimal collectionAmount;
	private BigDecimal rebateAmount;
	private String taxReason;
	private String taxPeriod;
	private String glCode;
	private Integer isActualDemand;
	private Date periodStartDate;
	private Date periodEndDate;
}
