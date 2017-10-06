package org.egov.egf.voucher.web.contract;

import java.util.Date;

import org.egov.common.web.contract.AuditableContract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VoucherSubTypeContract extends AuditableContract {

	private String id;

	private VoucherTypeContract voucherType;

	private String voucherName;

	private String voucherNamePrefix;

	private Date cutOffDate;

	private Boolean exclude;

}
