package org.egov.demand.model;

import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.slf4j.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DemandReason {
	
	@JsonProperty("name")
	private String name = null;

	@JsonProperty("category")
	private String category = null;

	@JsonProperty("taxPeriod")
	private String taxPeriod = null;

	@JsonProperty("glCode")
	private String glCode = null;
	
}
