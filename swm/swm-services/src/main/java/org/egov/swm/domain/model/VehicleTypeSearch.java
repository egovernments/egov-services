package org.egov.swm.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VehicleTypeSearch extends VehicleType {
    private String ids;
    private String sortBy;
    private Integer pageSize;
    private Integer offset;
}