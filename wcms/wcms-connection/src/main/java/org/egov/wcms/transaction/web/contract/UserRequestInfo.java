package org.egov.wcms.transaction.web.contract;

import org.egov.common.contract.request.RequestInfo;
import org.egov.wcms.transaction.model.User;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestInfo {
	@JsonProperty("User")
	private User user;
	@JsonProperty("RequestInfo")
	private RequestInfo RequestInfo;
}
