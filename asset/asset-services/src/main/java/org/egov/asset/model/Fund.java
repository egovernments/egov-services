package org.egov.asset.model;

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
public class Fund {

	private Long id;

	@Size(max = 50, min = 2)
	@NotNull
	private String name;

	@Size(max = 50, min = 2)
	@NotNull
	private String code;

	@NotNull
	private String identifier;

	@NotNull
	private Long level;

	private Boolean isParent;

	@NotNull
	private Boolean active;
}
