package org.egov.user.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.common.contract.request.RequestInfo;
import org.egov.user.domain.model.UpdatePassword;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UpdatePasswordRequest {
	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;
	private String existingPassword;
	private String newPassword;

	public UpdatePassword toDomain() {
		return UpdatePassword.builder()
				.existingPassword(existingPassword)
				.newPassword(newPassword)
				.userId(getUserId())
				.build();
	}

	private Long getUserId() {
		return requestInfo == null || requestInfo.getUserInfo() == null ? null : requestInfo.getUserInfo().getId();
	}
}

