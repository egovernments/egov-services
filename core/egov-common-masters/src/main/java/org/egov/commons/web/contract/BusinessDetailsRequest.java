package org.egov.commons.web.contract;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.commons.model.AuthenticatedUser;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class BusinessDetailsRequest {

	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;

	@JsonProperty("BusinessDetailsInfo")
	private BusinessDetailsRequestInfo businessDetails;

	public AuthenticatedUser toDomain() {
		User userInfo = requestInfo.getUserInfo();
		return AuthenticatedUser.builder().id(userInfo.getId()).anonymousUser(false).emailId(userInfo.getEmailId())
				.mobileNumber(userInfo.getMobileNumber()).name(userInfo.getName()).build();

	}

}
