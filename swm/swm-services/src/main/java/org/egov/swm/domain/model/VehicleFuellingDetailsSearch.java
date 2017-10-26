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
	private String ids;
	private String regNumber;
	private String vehicleTypeName;
	private String refuellingStationName;
	private String sortBy;
	private Integer pageSize;
	private Integer offset;

}