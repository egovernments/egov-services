package org.egov.asset.model;

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
public class EgfStatus {

	@NotNull
	private Long id;

	@NotNull
	private String moduleType;

	@NotNull
	private String code;

	@NotNull
	private String description;
}
