package org.egov.swm.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VehicleMaintenanceDetailsSearch extends VehicleMaintenanceDetails {
    private String transactionNos;
    private String regNumber;
    private String sortBy;
    private Integer pageSize;
    private Integer offset;
}
