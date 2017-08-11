package org.egov.tradelicense.web.requests;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.tradelicense.web.contract.Boundary;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoundaryResponse {

	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo = null;
	
	@JsonProperty("Boundary")
	private List<Boundary> boundarys = new ArrayList<Boundary>();

}