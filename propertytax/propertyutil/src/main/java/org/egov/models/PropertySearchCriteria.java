package org.egov.models;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PropertySearchCriteria {
	
	@NotNull
	@NotEmpty
	private String tenantId;
	
	private Boolean active;
	
	private String upicNumber;
	
	private Integer pageSize;
	
	private Integer pageNumber;
	
	private String[] sort;
	
	private String oldUpicNo;
	
	private String mobileNumber;
	
	private String aadhaarNumber;
	
	private String houseNoBldgApt;
	
	private String revenueZone;
	
	private String revenueWard;
	
	private String locality;
	
	private String ownerName;
	
	private Double demandFrom;
	
	private Double demandTo;
	
	private String propertyId;
	
	private String applicationNo;
	
	private String usage;
	
	private String adminBoundary;
	
	private String oldestUpicNo;
}
