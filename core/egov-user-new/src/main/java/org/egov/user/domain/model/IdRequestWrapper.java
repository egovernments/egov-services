package org.egov.user.domain.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class IdRequestWrapper {
	
	@JsonProperty("RequestInfo")
	public IdGenRequestInfo idGenRequestInfo;
	
	public List<IdRequest> idRequests;
}