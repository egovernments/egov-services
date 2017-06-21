package org.egov.asset.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.egov.asset.model.enums.VoucherType;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
public class Voucher {
	
	private Long id;
	
	@NotNull
	private VoucherType type;

	private String name;

	private String description;

	private String voucherNumber;

	@NotNull
	private String voucherDate;

	private Fund fund;

	private FiscalPeriod fiscalPeriod;

	private EgfStatus status;

	private Long originalVhId;

	private Long refVhId;

	private Long moduleId;
	
	private String cgvn;
	
	@NotNull
	private Long department;

	private List<VouchercreateAccountCodeDetails> ledgers = new ArrayList<>();

}
