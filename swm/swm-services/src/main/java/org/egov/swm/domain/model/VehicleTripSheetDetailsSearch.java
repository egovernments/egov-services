package org.egov.swm.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VehicleTripSheetDetailsSearch extends VehicleTripSheetDetails {
    private String tripNos;
    private String regNumber;
    private String routeCode;
    private String sortBy;
    private Integer pageSize;
    private Integer offset;
}