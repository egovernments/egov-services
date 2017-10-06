package org.egov.property.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BoundaryType {

	private String id;

	private String name;

	private String code;

	private String hierarchy;

	private String localName;

	private String parentName;

	private String childBoundaryTypes;

	private String tenantId;

	private String version;

	private String createdBy;

	private String createdDate;

	private String lastModifiedBy;

	private String lastModifiedDate;

}
