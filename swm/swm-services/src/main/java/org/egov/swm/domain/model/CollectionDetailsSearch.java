package org.egov.swm.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CollectionDetailsSearch extends CollectionDetails {

    private String collectionTypeCode = null;
    private String sourceSegregationCode = null;
    private String sourceSegregationCodes = null;
    private String sortBy;
    private Integer pageSize;
    private Integer offset;
}
