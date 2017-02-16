package org.egov.workflow.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ComplaintTypeResponse {

	private Long id;
	private String serviceName;
	private String serviceCode;

}
