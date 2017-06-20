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
public class Scheme {

	private Long id;

	private Long fund;

	@Size(max = 25, min = 1)
	private String code;

	@Size(max = 25, min = 1)
	private String name;

	@NotNull
	private Date validFrom;

	@NotNull
	private Date validTo;

	@NotNull
	private Boolean active;

	@Size(max = 256)
	private String description;

	private Long boundary;
}
