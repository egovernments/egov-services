package org.egov.asset.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.egov.asset.model.enums.VoucherType;

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

	private VoucherName name;

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

	private List<Ledger> ledgers = new ArrayList<>();

	private Vouchermis vouchermis;

}
