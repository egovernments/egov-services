package org.egov.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Yosadhara
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VacancyRemissionRequest {
	
	@JsonProperty("requestInfo")
    private RequestInfo requestInfo;
	
	@JsonProperty("remission")
	VacancyRemission vacancyRemission;
}
