package org.egov.asset.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
public class Ledger {

	private Long id;

	private Long orderId;

	private ChartOfAccountContract chartOfAccount;

	@NotNull
	@Size(max = 16)
	private String glcode;

	@NotNull
	private Double debitAmount;

	@NotNull
	private Double creditAmount;

	private Function function;

	private List<LedgerDetail> ledgerDetials = new ArrayList<>();
}
