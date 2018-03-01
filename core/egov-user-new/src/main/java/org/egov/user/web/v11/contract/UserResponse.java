package org.egov.user.web.v11.contract;

import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.user.domain.v11.model.User;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserResponse {
	private ResponseInfo responseInfo;
    private List<User> users;
}
