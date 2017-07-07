package org.egov.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DemandRequest {

	@NotNull
	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;
	
	@Valid
	@JsonProperty("Demands")
	private List<Demand> demands = new ArrayList<Demand>();
}
