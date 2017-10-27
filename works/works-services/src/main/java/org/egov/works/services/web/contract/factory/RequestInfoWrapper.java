package org.egov.works.services.web.contract.factory;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.works.services.web.contract.RequestInfo;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestInfoWrapper {

	@JsonProperty(value="RequestInfo")
	private RequestInfo requestInfo;
	
}