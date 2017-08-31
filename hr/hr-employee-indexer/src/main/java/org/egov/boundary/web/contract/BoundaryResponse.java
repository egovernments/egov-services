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
public class BoundaryResponse {

	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo = null;

	@JsonProperty("Boundary")
	private List<Boundary> boundarys = new ArrayList<Boundary>();

}
