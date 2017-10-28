package org.egov.swm.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RouteSearch extends Route {
	private String ids;
	private String collectionTypeCode;
	private String startingCollectionPointName;
	private String endingCollectionPointName;
	private String endingDumpingGroundPointName;
	private String sortBy;
	private Integer pageSize;
	private Integer offset;
}