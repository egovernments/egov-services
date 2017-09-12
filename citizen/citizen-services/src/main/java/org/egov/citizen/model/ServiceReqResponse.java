package org.egov.citizen.model;

import java.util.List;

import org.egov.common.contract.response.ResponseInfo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ServiceReqResponse {

	private ResponseInfo responseInfo;
	private ServiceReq serviceReq;
	//private List<ServiceReq> serviceRequests;

	
}
