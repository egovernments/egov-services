package org.egov.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GuidanceValueBoundaryResponse {
	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo;

	@JsonProperty("guidanceValueBoundaries")
	private List<GuidanceValueBoundary> guidanceValueBoundaries;
}
