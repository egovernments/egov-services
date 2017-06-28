package org.egov.asset.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

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

	@JsonProperty("id")
	private Long id;

	@Size(max = 128, min = 2)
	@JsonProperty("name")
	private String name;

	@Size(max = 16, min = 2)
	@JsonProperty("code")
	private String code;

	@JsonProperty("level")
	private Long level;

	@JsonProperty("active")
	private Boolean active;

	@JsonProperty("isParent")
	private Boolean isParent;

	@JsonProperty("parentId")
	private Long parentId;
}
