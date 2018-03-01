package org.egov.user.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.common.contract.request.RequestInfo;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class LoggedInUserUpdatePasswordRequest {
	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;
	private String existingPassword;
	private String newPassword;
	private String tenantId;

	public org.egov.user.domain.model.LoggedInUserUpdatePasswordRequest toDomain() {
		return org.egov.user.domain.model.LoggedInUserUpdatePasswordRequest.builder()
				.existingPassword(existingPassword)
				.newPassword(newPassword)
				.userId(getUserId())
				.tenantId(tenantId)
				.build();
	}

	private Long getUserId() {
		return requestInfo == null || requestInfo.getUserInfo() == null ? null : requestInfo.getUserInfo().getId();
	}
}

