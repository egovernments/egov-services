package org.egov.lams.model;

public class DemandDetails {
	private String taxAmount;
	private String collectionAmount;
	private String rebateAmount;
	private String taxReason;
	private String taxPeriod;
	private String glCode;

	public String getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(String taxAmount) {
		this.taxAmount = taxAmount;
	}

	public String getCollectionAmount() {
		return collectionAmount;
	}

	public void setCollectionAmount(String collectionAmount) {
		this.collectionAmount = collectionAmount;
	}

	public String getRebateAmount() {
		return rebateAmount;
	}

	public void setRebateAmount(String rebateAmount) {
		this.rebateAmount = rebateAmount;
	}

	public String getTaxReason() {
		return taxReason;
	}

	public void setTaxReason(String taxReason) {
		this.taxReason = taxReason;
	}

	public String getTaxPeriod() {
		return taxPeriod;
	}

	public void setTaxPeriod(String taxPeriod) {
		this.taxPeriod = taxPeriod;
	}

	public String getGlCode() {
		return glCode;
	}

	public void setGlCode(String glCode) {
		this.glCode = glCode;
	}

}
