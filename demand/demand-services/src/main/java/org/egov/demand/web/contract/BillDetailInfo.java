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
public class BillDetailInfo {
	private Long billId = null;
	private BigDecimal creditAmount = null;
	private BigDecimal debitAmount = null;
	private BigDecimal amountCollected = null;
	private String glCode = null;
	private String functionCode = null;
	private String description = null;
	private String purpose = null;
	private String period = null;
	private Integer orderNo = null;
	private Integer groupId =null;
	private Integer isActualDemand = null;

}
