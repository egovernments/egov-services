package org.egov.egf.voucher.domain.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VoucherSearch extends Voucher {

	private String ids;
	
	private String sortBy;
	
	private Integer pageSize;
	
	private Integer offset;
	
	private String glcode;

	private BigDecimal debitAmount;

	private BigDecimal creditAmount;

	private String accountDetailTypeId;

	private String accountDetailKeyId;

	private BigDecimal subLedgerAmount;
}