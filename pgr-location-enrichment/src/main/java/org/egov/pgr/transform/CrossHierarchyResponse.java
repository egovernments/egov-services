package org.egov.pgr.transform;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@AllArgsConstructor
public class CrossHierarchyResponse {

	private BoundaryResponse parent;

	private BoundaryResponse child;

}
