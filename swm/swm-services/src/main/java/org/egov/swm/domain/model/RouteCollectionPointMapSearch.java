package org.egov.swm.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RouteCollectionPointMapSearch extends RouteCollectionPointMap {
    private String routes;
    private String endingDumpingGroundPointCode;
    private String collectionPointCode;
    private String sortBy;
    private Integer pageSize;
    private Integer offset;
}