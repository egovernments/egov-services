package org.egov.boundary.web.contract;

import java.util.ArrayList;
import java.util.List;

import lombok.*;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.egov.common.contract.response.ResponseInfo;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoundaryTypeResponse {

	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo = null;

	@JsonProperty("BoundaryType")
	private List<BoundaryType> boundaryTypes = new ArrayList<BoundaryType>();

}
