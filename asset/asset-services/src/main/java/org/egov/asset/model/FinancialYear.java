package org.egov.asset.model;

import java.util.Date;

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
public class FinancialYear {

	private Long id;

	@Size(min = 1, max = 25)
	private String finYearRange;

	@NotNull
	private Date startingDate;

	@NotNull
	private Date endingDate;

	@NotNull
	private Boolean active;

	@NotNull
	private Boolean isActiveForPosting;

	private Boolean isClosed;

	private Boolean transferClosingBalance;
}
