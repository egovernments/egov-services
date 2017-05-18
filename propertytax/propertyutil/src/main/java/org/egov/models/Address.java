package org.egov.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * Address
 */

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Address {

	private String id;

	private String houseNoBldgApt;

	private String streetRoadLine;

	private String landmark;

	private String areaLocalitySector;

	private String cityTownVillage;

	private String district;

	private String subDistrict;

	private String postOffice;

	private String state;

	private String country;

	private String pinCode;

	private String type;

	@NonNull
	private String tenantId;

	private String createdBy;

	private String createdDate;

	private String lastModifiedBy;

	private String lastModifiedDate;

}
