package org.egov.infra.mdms.model;

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
	private RequestInfo requestInfo;
	
	@JsonProperty("MdmsCriteria")
	private MdmsCriteria mdmsCriteria;

}
