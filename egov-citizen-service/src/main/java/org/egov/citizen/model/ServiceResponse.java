package org.egov.citizen.model;

import java.util.List;

import org.egov.common.contract.response.ResponseInfo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ServiceResponse {
	
	private ResponseInfo responseInfo;
	private List<ServiceDefination> services;
}
