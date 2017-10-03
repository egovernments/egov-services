package org.egov.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VacancyRemissionResponse {
	
	@JsonProperty("responseInfo")
	private ResponseInfo responseInfo;
	
	@JsonProperty("remission")
	private VacancyRemission vacancyRemission;
}
