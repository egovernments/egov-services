package org.egov.lcms.models;

import java.util.List;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgencyRequest {

	@Valid
	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;
	
	@Valid
	@JsonProperty("agencies")
	private List<Agency> agencies;

}
