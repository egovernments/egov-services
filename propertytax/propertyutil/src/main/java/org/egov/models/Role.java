package org.egov.models;

import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Role
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Role {
	@Size(message="",min=1, max=32)
	private String name;

	private String description;

	@Size(message="",min=1, max=32)
	private String code;

	private String id;

	private Integer createdBy;

	private String createdDate;

	private Integer lastModifiedBy;

	private String lastModifiedDate;

	private String tenantId;

}
