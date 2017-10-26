package io.swagger.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaseRequest {
	@JsonProperty("requestInfo")
	private RequestInfo requestInfo = null;

	@JsonProperty("cases")
	private List<Case> cases = null;
}