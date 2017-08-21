package org.egov.citizen.model;

import org.egov.common.contract.response.ResponseInfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceReqResponse {

	private ResponseInfo responseInfo;
	private ServiceReq serviceReq;
	
}
