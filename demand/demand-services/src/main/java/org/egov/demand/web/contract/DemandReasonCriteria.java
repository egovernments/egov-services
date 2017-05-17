package org.egov.demand.web.contract;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;


public class DemandReasonCriteria {
	private String moduleName;
	private String taxCategory;
	private String taxReason;
	private String taxPeriod;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date fromDate;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date toDate;
	private String tenantId;

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getTaxCategory() {
		return taxCategory;
	}

	public void setTaxCategory(String taxCategory) {
		this.taxCategory = taxCategory;
	}

	public String getTaxPeriod() {
		return taxPeriod;
	}

	public void setTaxPeriod(String taxPeriod) {
		this.taxPeriod = taxPeriod;
	}

	public String getTaxReason() {
		return taxReason;
	}

	public void setTaxReason(String taxReason) {
		this.taxReason = taxReason;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

}
