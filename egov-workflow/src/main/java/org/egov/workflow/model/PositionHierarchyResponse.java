package org.egov.workflow.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PositionHierarchyResponse {

	private Long id;

	private PositionResponse fromPosition;

	private PositionResponse toPosition;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PositionResponse getFromPosition() {
		return fromPosition;
	}

	public void setFromPosition(PositionResponse fromPosition) {
		this.fromPosition = fromPosition;
	}

	public PositionResponse getToPosition() {
		return toPosition;
	}

	public void setToPosition(PositionResponse toPosition) {
		this.toPosition = toPosition;
	}

}
