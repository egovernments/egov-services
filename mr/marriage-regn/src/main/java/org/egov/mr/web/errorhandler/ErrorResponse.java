package org.egov.mr.web.errorhandler;

import org.egov.common.contract.response.ResponseInfo;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@Builder
public class ErrorResponse {
	private ResponseInfo responseInfo;

	private Error error;
}
