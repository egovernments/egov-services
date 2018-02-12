package org.egov.swm.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VehicleFuellingDetailsSearch extends VehicleFuellingDetails {

    private String regNumber;
    private String fuelTypeCode;
    private String vehicleTypeCode;
    private String refuellingStationName;
    private Long transactionFromDate;
    private Long transactionToDate;
    private Double costIncurredFrom;
    private Double costIncurredTo;
    private String sortBy;
    private Integer pageSize;
    private Integer offset;

}