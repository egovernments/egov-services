package org.egov.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AssessmentDate {

	@JsonProperty("name")
	private AssessmentDateEnum name = null;

	@JsonProperty("date")
	private String date = null;

}
