package org.egov.user.web.contract;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.egov.user.domain.model.Role;

import java.util.Date;

@Getter
@Builder
@AllArgsConstructor
public class RoleRequest {
	private Long id;
	private String name;
	private String code;
	private String description;
	private Long createdBy;
	@JsonFormat(pattern = "MM/dd/yyyy")
	private Date createdDate;
	private Long lastModifiedBy;
	@JsonFormat(pattern = "MM/dd/yyyy")
	private Date lastModifiedDate;

	public RoleRequest(org.egov.user.persistence.entity.Role roleEntity) {
		this.id = roleEntity.getId();
		this.name = roleEntity.getName();
		this.description = roleEntity.getDescription();
		this.createdBy = roleEntity.getCreatedBy() == null ? 0L : roleEntity.getCreatedBy();
		this.createdDate = roleEntity.getCreatedDate();
		this.lastModifiedBy = roleEntity.getLastModifiedBy() == null ? 0L : roleEntity.getLastModifiedBy();
		this.lastModifiedDate = roleEntity.getLastModifiedDate();
	}

	public RoleRequest(Role domainRole) {
		this.id = domainRole.getId();
		this.name = domainRole.getName();
		this.description = domainRole.getDescription();
		this.createdBy = domainRole.getCreatedBy() == null ? 0L : domainRole.getCreatedBy();
		this.createdDate = domainRole.getCreatedDate();
		this.lastModifiedBy = domainRole.getLastModifiedBy() == null ? 0L : domainRole.getLastModifiedBy();
		this.lastModifiedDate = domainRole.getLastModifiedDate();
	}

	public Role toDomain() {
		return Role.builder()
				.code(code)
				.build();
	}
}
