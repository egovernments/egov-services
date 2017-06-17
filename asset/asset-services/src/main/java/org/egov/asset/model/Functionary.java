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
public class Functionary {

	private Long id;

	@NotNull
	@Size(max = 16, min = 1)
	private String code;

	@NotNull
	@Size(max = 256, min = 1)
	private String name;

	@NotNull
	private Boolean active;

}
