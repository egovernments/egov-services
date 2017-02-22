package org.egov.user.web.contract;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {

	private ResponseInfo responseInfo;
	private Error error;
}
