package org.egov.pgr.common.contract;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.egov.pgr.read.web.contract.ResponseInfo;
import org.egov.pgr.read.web.contract.User;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class GetUserByIdResponse {
	@JsonProperty("responseInfo")
    ResponseInfo responseInfo;

	@JsonProperty("user")
	List<User> user;
}
