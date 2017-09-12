package org.egov.tradelicense.domain.model;

import javax.validation.constraints.NotNull;

import org.egov.tradelicense.domain.enums.BusinessNature;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LicenseSearch {

	@NotNull
	private String tenantId;

	private String active;

	private Integer[] ids;

	private String applicationNumber;

	private String licenseNumber;

	private String oldLicenseNumber;

	private String mobileNumber;

	private String aadhaarNumber;

	private String emailId;

	private String propertyAssesmentNo;

	private Integer adminWard;

	private Integer locality;

	private String ownerName;

	private String tradeTitle;

	private String tradeType;

	private Integer tradeCategory;

	private Integer tradeSubCategory;

	private String legacy;

	private Integer status;

	private Integer applicationStatus;

	private Integer pageSize;

	private Integer pageNumber;

	private String sort;
}