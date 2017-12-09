package org.egov.mdms.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.egov.common.contract.request.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MDMSReloadReq {
	
	@JsonProperty("RequestInfo")
	@Valid
	@NotNull
	private RequestInfo requestInfo;
	
	@JsonProperty("MdmsReq")
	@Valid
	@NotNull
	private Object mdmsReq;

}
