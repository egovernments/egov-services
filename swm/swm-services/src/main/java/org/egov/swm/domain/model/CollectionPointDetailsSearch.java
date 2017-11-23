package org.egov.swm.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CollectionPointDetailsSearch extends CollectionPointDetails {
    private String ids;
    private String collectionTypeCode;
    private String collectionPoint;
    private String collectionPoints;
    private String sortBy;
    private Integer pageSize;
    private Integer offset;
}