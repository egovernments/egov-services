package org.egov.pgrrest.common.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.egov.pgrrest.read.web.contract.ResponseInfo;
import org.egov.pgrrest.read.web.contract.User;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class GetUserByIdResponse {

    ResponseInfo responseInfo;

	List<User> user;
}
