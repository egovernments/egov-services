package org.egov.demand.web.contract;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class InstallmentSearchCriteria {

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date fromDate = null;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date toDate = null;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date currentDate = null;
	private Date installmentYear = null;
	private String module = null;
	private String installmentNumber = null;
	private String description = null;
	private String installmentType = null;
	private String financialYear = null;
	private String tenantId;

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Date getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}

	public Date getInstallmentYear() {
		return installmentYear;
	}

	public void setInstallmentYear(Date installmentYear) {
		this.installmentYear = installmentYear;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getInstallmentNumber() {
		return installmentNumber;
	}

	public void setInstallmentNumber(String installmentNumber) {
		this.installmentNumber = installmentNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getInstallmentType() {
		return installmentType;
	}

	public void setInstallmentType(String installmentType) {
		this.installmentType = installmentType;
	}

	public String getFinancialYear() {
		return financialYear;
	}

	public void setFinancialYear(String financialYear) {
		this.financialYear = financialYear;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

}
