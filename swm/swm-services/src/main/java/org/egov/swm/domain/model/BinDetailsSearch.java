package org.egov.swm.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BinDetailsSearch extends BinDetails {
    private String collectionPoint;
    private String collectionPoints;
    private String assetCode;
    private String sortBy;
    private Integer pageSize;
    private Integer offset;
}