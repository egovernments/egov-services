package org.egov.workflow.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComplaintTypeResponse {

	private Long id;
	private String serviceName;
	private String serviceCode;
    private String tenantId;
}
