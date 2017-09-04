package org.egov.citizen.web.contract;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PGPayLoadResWrapper {
	
	@JsonProperty("PGResponse")
	@NotNull
	private PGPayloadResponse PGPayloadResponse;

}
