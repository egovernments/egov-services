package org.egov.tradelicense.persistence.entity;

import org.egov.tradelicense.domain.model.LicenseSearch;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Builder
public class LicenseSearchEntity {

	public static final String TABLE_NAME = "egtl_license";

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

	private String adminWard;

	private String locality;

	private String ownerName;

	private String tradeTitle;

	private String tradeType;

	private String tradeCategory;

	private String tradeSubCategory;

	private String legacy;

	private String status;

	private String applicationStatus;

	private Integer pageSize;

	private Integer pageNumber;

	private String sort;

	public LicenseSearchEntity toEntity(final LicenseSearch licenseSearch) {

		aadhaarNumber = licenseSearch.getAadhaarNumber();
		active = licenseSearch.getActive();
		adminWard = licenseSearch.getAdminWard();
		applicationNumber = licenseSearch.getApplicationNumber();
		applicationStatus = licenseSearch.getApplicationStatus();
		emailId = licenseSearch.getEmailId();
		ids = licenseSearch.getIds();
		legacy = licenseSearch.getLegacy();
		licenseNumber = licenseSearch.getLicenseNumber();
		locality = licenseSearch.getLocality();
		mobileNumber = licenseSearch.getMobileNumber();
		oldLicenseNumber = licenseSearch.getOldLicenseNumber();
		ownerName = licenseSearch.getOwnerName();
		pageNumber = licenseSearch.getPageNumber();
		pageSize = licenseSearch.getPageSize();
		propertyAssesmentNo = licenseSearch.getPropertyAssesmentNo();
		sort = licenseSearch.getSort();
		status = licenseSearch.getStatus();
		tenantId = licenseSearch.getTenantId();
		tradeCategory = licenseSearch.getTradeCategory();
		tradeSubCategory = licenseSearch.getTradeSubCategory();
		tradeTitle = licenseSearch.getTradeTitle();
		tradeType = licenseSearch.getTradeType();

		return this;
	}
}