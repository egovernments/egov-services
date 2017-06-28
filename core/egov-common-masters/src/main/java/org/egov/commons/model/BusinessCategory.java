package org.egov.commons.model;

import java.util.Date;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class BusinessCategory {

	@NotNull
	private Long id;

	@NotNull
	private String name;

	@NotNull
	private String code;

	@NotNull
	private Boolean isactive;

	@NotNull
	private String tenantId;

	@NotNull
	private Long createdBy;

	private Date createdDate;

	@NotNull
	private Long lastModifiedBy;

	private Date lastModifiedDate;

	public BusinessCategory(org.egov.commons.web.contract.BusinessCategory category) {

		id = category.getId();
		name = category.getName();
		code = category.getCode();
		isactive = category.getActive();
		tenantId = category.getTenantId();

	}

	public org.egov.commons.web.contract.BusinessCategory toDomain() {
		return org.egov.commons.web.contract.BusinessCategory.builder().id(id).name(name).code(code).active(isactive)
				.tenantId(tenantId).build();

	}
}
