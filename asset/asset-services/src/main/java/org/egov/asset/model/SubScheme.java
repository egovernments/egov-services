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
public class SubScheme {

	private Long id;

	@NotNull
	private Long scheme;

	@NotNull
	@Size(max = 50, min = 1)
	private String code;

	@NotNull
	@Size(max = 50, min = 1)
	private String name;

	@NotNull
	private Date validFrom;

	@NotNull
	private Date validTo;

	@NotNull
	private Boolean active;

	private Long departmentId;
}
