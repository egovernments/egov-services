package org.egov.collection.model;

import java.util.List;
import org.egov.common.contract.request.RequestInfo;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Getter
@Setter
@ToString
public class IdRequestWrapper {

	@JsonProperty("RequestInfo")
	public RequestInfo requestInfo;
	
	public List<IdRequest> idRequests;
}
