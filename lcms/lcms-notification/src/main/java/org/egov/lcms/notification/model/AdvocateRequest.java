package org.egov.lcms.notification.model;

import java.util.List;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdvocateRequest {
	
	@Valid
	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo = null;
	
	@Valid
	@JsonProperty("advocates")
	private List<Advocate> advocates = null;
}
