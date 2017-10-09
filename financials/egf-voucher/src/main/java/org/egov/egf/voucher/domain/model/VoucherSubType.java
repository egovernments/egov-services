package org.egov.egf.voucher.domain.model;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.egov.common.domain.model.Auditable;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class VoucherSubType extends Auditable {

	private String id;

	@NotNull
	private VoucherType voucherType;

	@NotEmpty
	@Length(max = 50)
	private String voucherName;

	@NotEmpty
	@Length(max = 50)
	private String voucherNamePrefix;

	private Date cutOffDate;

	private Boolean exclude;
}
