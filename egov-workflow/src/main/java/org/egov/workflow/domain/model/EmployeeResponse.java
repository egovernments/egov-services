package org.egov.workflow.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeResponse {

	private Long id;
	private String name;
	private String code;

}
