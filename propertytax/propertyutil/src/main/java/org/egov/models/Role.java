package org.egov.models;

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

	private String name;

	private String description;

	private String code;

	private String id;

	private Integer createdBy;

	private String createdDate;

	private Integer lastModifiedBy;

	private String lastModifiedDate;

	private String tenantId;

}
