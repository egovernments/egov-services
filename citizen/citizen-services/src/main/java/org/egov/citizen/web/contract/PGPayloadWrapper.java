package org.egov.citizen.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PGPayloadWrapper {
	
	@JsonProperty("PGRequest")
	private PGPayload pgPayLoad;

}
