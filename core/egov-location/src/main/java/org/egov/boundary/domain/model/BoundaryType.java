package org.egov.boundary.domain.model;

import java.util.Set;

import javax.validation.constraints.NotNull;

import org.egov.boundary.web.contract.HierarchyType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoundaryType {

	private Long id;
	private String name;
	private String code;
	private HierarchyType hierarchyType;
	private BoundaryType parent;
	private Long hierarchy;
	private String localName;
	private String parentName;
	private Set<BoundaryType> childBoundaryTypes;
	@NotNull
	private String tenantId;

}
