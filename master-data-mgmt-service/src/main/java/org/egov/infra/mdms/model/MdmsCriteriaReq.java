package org.egov.infra.mdms.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.egov.common.contract.request.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class MdmsCriteriaReq {
	
	@JsonProperty("RequestInfo")
	@Valid
	@NotNull
	private RequestInfo requestInfo;
	
	@JsonProperty("MdmsCriteria")
	@Valid
	@NotNull
	private MdmsCriteria mdmsCriteria;

}
