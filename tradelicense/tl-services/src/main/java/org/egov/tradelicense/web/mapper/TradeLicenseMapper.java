package org.egov.tradelicense.web.mapper;

import org.egov.tl.commons.web.contract.LicenseSearchContract;
import org.egov.tradelicense.domain.model.LicenseSearch;

public class TradeLicenseMapper {

	public LicenseSearch toSearchDomain(final LicenseSearchContract contract) {

		final LicenseSearch licenseSearch = new LicenseSearch();

		licenseSearch.setAadhaarNumber(contract.getAadhaarNumber());
		licenseSearch.setActive(contract.getActive());
		licenseSearch.setAdminWard(contract.getAdminWard());
		licenseSearch.setApplicationNumber(contract.getApplicationNumber());
		licenseSearch.setApplicationStatus(contract.getApplicationStatus());
		licenseSearch.setEmailId(contract.getEmailId());
		licenseSearch.setIds(contract.getIds());
		licenseSearch.setLegacy(contract.getLegacy());
		licenseSearch.setLicenseNumber(contract.getLicenseNumber());
		licenseSearch.setLocality(contract.getLocality());
		licenseSearch.setMobileNumber(contract.getMobileNumber());
		licenseSearch.setOldLicenseNumber(contract.getOldLicenseNumber());
		licenseSearch.setOwnerName(contract.getOwnerName());
		licenseSearch.setPageNumber(contract.getPageNumber());
		licenseSearch.setPageSize(contract.getPageSize());
		licenseSearch.setPropertyAssesmentNo(contract.getPropertyAssesmentNo());
		licenseSearch.setSort(contract.getSort());
		licenseSearch.setStatus(contract.getStatus());
		licenseSearch.setTenantId(contract.getTenantId());
		licenseSearch.setTradeCategory(contract.getTradeCategory());
		licenseSearch.setTradeSubCategory(contract.getTradeSubCategory());
		licenseSearch.setTradeTitle(contract.getTradeTitle());
		licenseSearch.setTradeType(contract.getTradeType());

		return licenseSearch;
	}
}