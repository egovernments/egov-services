package org.egov.egf.voucher.domain.model;

import java.math.BigDecimal;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.egov.egf.master.web.contract.AccountDetailKeyContract;
import org.egov.egf.master.web.contract.AccountDetailTypeContract;

public class DeductionDetail {
      
        private String id;

	private AccountDetailTypeContract accountDetailType;

	@NotNull
	private AccountDetailKeyContract accountDetailKey;

	@NotNull
	@Min(1)
	@Max(value = 999999999)
	private BigDecimal amount;
	
	@Min(0)
	@Max(value = 999999999)
	private BigDecimal remittedAmount;
}
