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
    private String codes;
    private String collectionTypeCode;
    private String startingCollectionPointCode;
    private String endingCollectionPointCode;
    private String endingDumpingGroundPointCode;
    private String sortBy;
    private Integer pageSize;
    private Integer offset;
}