package org.egov.citizen.model;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DemandRequest {

	@NotNull
	@JsonProperty("RequestInfo")
	private String requestInfo;
	
	@Valid
	@NotNull
	@JsonProperty("Demands")
	private List<Demand> demands;
}