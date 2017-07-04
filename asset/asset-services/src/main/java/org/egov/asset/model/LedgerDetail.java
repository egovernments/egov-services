package org.egov.asset.model;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

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
public class LedgerDetail {

	private Long id;

	private AccountDetailType accountDetailType;

	private AccountDetailKey accountDetailKey;

	@NotNull
	private BigDecimal amount;
}
