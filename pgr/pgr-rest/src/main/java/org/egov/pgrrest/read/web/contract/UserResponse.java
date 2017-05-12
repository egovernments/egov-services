package org.egov.pgrrest.read.web.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.egov.common.contract.response.ResponseInfo;

import java.util.List;

@AllArgsConstructor
@Getter
public class UserResponse {
    private ResponseInfo responseInfo;
    private List<User> user;
}
