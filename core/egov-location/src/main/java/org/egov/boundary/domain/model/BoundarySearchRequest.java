package org.egov.boundary.domain.model;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BoundarySearchRequest {

	private String tenantId;
	private List<String> codes;
	private List<Long> boundaryTypeIds;
	private List<Long> boundaryNumbers;
	private List<Long> hierarchyTypeIds;
	private String hierarchyTypeName;
	private List<Long> boundaryIds;
	
}
