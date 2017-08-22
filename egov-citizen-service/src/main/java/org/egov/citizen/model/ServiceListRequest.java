package org.egov.citizen.model;

import java.util.List;

import org.egov.common.contract.request.RequestInfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceListRequest {

	private RequestInfo requestInfo;
	private List<ServiceReq> serviceReq;
	
}
