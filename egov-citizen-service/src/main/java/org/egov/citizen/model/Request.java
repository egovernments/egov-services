package org.egov.citizen.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Request {

	private String requestInfo;
	private List<IdRequest> idRequests;
	
}
