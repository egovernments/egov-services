package org.egov.tradelicense.web.contract;

import java.util.List;

import org.egov.common.contract.response.ResponseInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserResponseInfo {
	
	private List<User> user;

	private ResponseInfo responseInfo;

}
