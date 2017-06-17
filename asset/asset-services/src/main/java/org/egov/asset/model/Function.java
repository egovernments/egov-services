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
public class Function {

	private Long id;

	@Size(max = 128, min = 2)
	@NotNull
	private String name;

	@Size(max = 16, min = 2)
	@NotNull
	private String code;

	@NotNull
	private Integer level;

	@NotNull
	private Boolean active;

	@NotNull
	private Boolean isParent;

	private Long parentId;
}
