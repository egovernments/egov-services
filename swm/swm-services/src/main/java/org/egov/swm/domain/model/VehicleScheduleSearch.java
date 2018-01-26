package org.egov.swm.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VehicleScheduleSearch extends VehicleSchedule {
    private String transactionNos;
    private String routeCode;
    private String regNumber;
    private Boolean fromTripSheet;
    private String sortBy;
    private Integer pageSize;
    private Integer offset;
}