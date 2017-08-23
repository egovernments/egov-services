package org.egov.citizen.model;

import java.util.List;

import org.egov.common.contract.response.ResponseInfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceReqResponse {

	private ResponseInfo responseInfo;
	private ServiceReq serviceReq;
	private List<ServiceReq> serviceRequests;

	
}
