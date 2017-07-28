package org.egov.collection.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Getter
@Setter
@ToString
public class IdRequestWrapper {
	
	@JsonProperty("RequestInfo")
	public IdGenRequestInfo idGenRequestInfo;
	
	public List<IdRequest> idRequests;
}
