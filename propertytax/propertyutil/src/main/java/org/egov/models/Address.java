package org.egov.models;



import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Address
 */

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Address {

	
	private Integer id;

	@Valid
	@Size(min=4, max= 32)
	private String houseNoBldgApt;



	@Size(min=4, max= 256)
	private String streetRoadLine;
	
	
	@Size(min=4, max= 256)
	private String landmark;

	
	@Size(min=4, max= 256)
	private String areaLocalitySector;
	
	@Size(min=4, max= 256)
	private String cityTownVillage;
	
	@Size(min=4, max= 100)
	private String district;
	
	@Size(min=4, max= 100)
	private String subDistrict;
	
	@Size(min=4, max= 100)
	private String postOffice;
	
	@Size(min=4, max= 100)
	private String state;

	@Size(min=4, max= 50)
	private String country;
	
	@Size(min=6, max= 10)
	private String pinCode;
	
	@Size(min=4, max= 50)
	private String type;

	@NotNull
	@Size(min=4, max= 128)
	private String tenantId;
	
	private AuditDetails auditDetails;

}
