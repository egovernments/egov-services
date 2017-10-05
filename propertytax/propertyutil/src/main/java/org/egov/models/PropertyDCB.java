package org.egov.models;

import java.util.List;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PropertyDCB {

	@JsonProperty("upicNumber")
	@Size(min = 6, max = 128)
	private String upicNumber;

	@JsonProperty("oldUpicNumber")
	@Size(min = 4, max = 128)
	private String oldUpicNumber;

	@JsonProperty("Demands")
	private List<Demand> demands;
}
