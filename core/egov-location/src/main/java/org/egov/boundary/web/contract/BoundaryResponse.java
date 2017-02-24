package org.egov.boundary.web.contract;

import java.util.ArrayList;
import java.util.List;

import org.egov.boundary.persistence.entity.Boundary;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BoundaryResponse {
	
	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo = null;
	@JsonProperty("Boundary")
	private List<Boundary> boundarys = new ArrayList<Boundary>();

	public ResponseInfo getResponseInfo() {
		return responseInfo;
	}

	public void setResponseInfo(ResponseInfo responseInfo) {
		this.responseInfo = responseInfo;
	}

	public List<Boundary> getBoundarys() {
		return boundarys;
	}

	public void setBoundarys(List<Boundary> boundarys) {
		this.boundarys = boundarys;
	}

	

	
	 
}
