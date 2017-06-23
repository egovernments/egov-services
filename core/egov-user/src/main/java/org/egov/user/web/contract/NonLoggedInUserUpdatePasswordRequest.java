package org.egov.user.web.contract;

import lombok.*;
import org.codehaus.jackson.annotate.JsonProperty;
import org.egov.common.contract.request.RequestInfo;

/*
	Update password request by non logged in user
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class NonLoggedInUserUpdatePasswordRequest {

	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;
	private String otpReference;
	private String userName;
	private String newPassword;
	private String tenantId;

	public org.egov.user.domain.model.NonLoggedInUserUpdatePasswordRequest toDomain() {
		return org.egov.user.domain.model.NonLoggedInUserUpdatePasswordRequest.builder().otpReference(otpReference)
				.userName(userName).newPassword(newPassword).tenantId(tenantId).build();
	}
}
