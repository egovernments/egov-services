package org.egov.boundary.web.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.egov.boundary.domain.model.Boundary;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TenantBoundarySearch {

	private HierarchyType hierarchyType;
	private Boundary boundary;
	private String tenantId;
}
