package org.egov.workflow.model;

import org.egov.workflow.model.ResponseInfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PositionResponse {

	private Long id;

	private String name;

	@JsonProperty("responseInfo")
	private ResponseInfo responseInfo = null;

	public PositionResponse() {

	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public ResponseInfo getResponseInfo() {
		return responseInfo;
	}

	public void setResponseInfo(final ResponseInfo responseInfo) {
		this.responseInfo = responseInfo;
	}

}
