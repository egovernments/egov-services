package org.egov.lams.web.contract;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Boundary {

	@NotEmpty
	@JsonProperty("id")
	private Long id;
	@JsonProperty("name")
	private String name;
	@JsonProperty("longitude")
	private Float longitude;
	@JsonProperty("latitude")
	private Float latitude;
	@JsonProperty("boundaryNum")
	private Long boundaryNum;

	private Boundary parent;
}