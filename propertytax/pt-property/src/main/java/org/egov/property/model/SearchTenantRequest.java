package org.egov.property.model;

import org.egov.models.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SearchTenantRequest {
	
	@JsonProperty("RequestInfo")
    private RequestInfo requestInfo;
}
