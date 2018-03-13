package org.egov.user.web.contract;

import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.user.web.contract.User;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserResponse {
	private ResponseInfo responseInfo;
	private List<User> users;
}
