package org.egov.workflow.web.contract;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PositionHierarchyResponse {

	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo;

	private List<EscalationHierarchy> escalationHierarchies;

}