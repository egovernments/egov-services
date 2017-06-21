package org.egov.asset.model;

import java.math.BigDecimal;

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
public class FundSource {

	private Long id;

	@Size(min = 1, max = 25)
	@NotNull
	private String code;

	@Size(min = 1, max = 25)
	@NotNull
	private String name;

	@Size(min = 1, max = 25)
	private String type;

	private Long fundSource;

	private BigDecimal llevel;

	@NotNull
	private Boolean active;

	private Boolean isParent;
}
