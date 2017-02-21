package org.egov.pgr.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CrossHierarchyResponse {
	private BoundaryResponse parent;
	private BoundaryResponse child;

}
