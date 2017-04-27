package org.egov.user.web.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.egov.user.domain.model.Role;

@Getter
@Builder
@AllArgsConstructor
public class RoleRequest {
	private Long id;
	private String code;
	private String name;

	public RoleRequest(Role domainRole) {
		this.id = domainRole.getId();
		this.code = domainRole.getCode();
		this.name = domainRole.getName();
	}

	public Role toDomain() {
		return Role.builder()
				.code(code)
				.build();
	}
}
