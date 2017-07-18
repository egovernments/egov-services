package org.egov.asset.model;

import org.hibernate.validator.constraints.Length;

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
public class StatusValue {
	
	@JsonProperty("name")
	@Length(min = 3, max = 20)
	private String name;
	
	@JsonProperty("code")
	@Length(min = 3, max = 20)
	private String code;

	@JsonProperty("description")
	@Length(min = 3, max = 250)
	private String description;

}
