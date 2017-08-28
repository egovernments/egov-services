package org.egov.egf.voucher.domain.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.egov.common.domain.model.Auditable;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class VoucherSubType extends Auditable {
	
	private String id;

	private VoucherType voucherType;
	
	@Length(max = 50)
	private String voucherName;
	
	private Date cutOffDate;
	
	private Boolean exclude;
}
