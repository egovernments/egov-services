package org.egov.pgr.web.contract;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
