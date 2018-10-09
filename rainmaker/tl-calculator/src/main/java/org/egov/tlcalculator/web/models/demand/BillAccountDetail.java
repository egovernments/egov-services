package org.egov.tlcalculator.web.models.demand;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.egov.tlcalculator.web.models.enums.Purpose;

import java.math.BigDecimal;

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

	@Default
	private BigDecimal creditAmount = BigDecimal.ZERO;

	@Default
	private BigDecimal debitAmount = BigDecimal.ZERO;

	private Boolean isActualDemand;

	private Purpose purpose;
	
	public String getTaxHeadCode() {
		if(!StringUtils.isEmpty(this.accountDescription))
			return this.accountDescription.split("[-]")[0];
		else
			return null;
	}
}