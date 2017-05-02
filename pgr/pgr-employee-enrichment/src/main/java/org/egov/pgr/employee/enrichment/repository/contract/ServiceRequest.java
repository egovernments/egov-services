package org.egov.pgr.employee.enrichment.repository.contract;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ServiceRequest {

	@JsonProperty("serviceCode")
	private String complaintTypeCode;

	@JsonProperty("description")
	private String description;

	@JsonProperty("values")
	private Map<String, String> values = new HashMap<>();

}
