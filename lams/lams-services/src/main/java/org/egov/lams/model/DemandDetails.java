package org.egov.lams.model;

import java.math.BigDecimal;

/**
 * @author ramki
 *
 */
public class DemandDetails {
	private BigDecimal taxAmount;
	private BigDecimal collectionAmount;
	private BigDecimal rebateAmount;
	private String taxReason;
	private String taxPeriod;
	private Long glCode;
	private Integer isActualDemand;

	public BigDecimal getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
	}

	public BigDecimal getCollectionAmount() {
		return collectionAmount;
	}

	public void setCollectionAmount(BigDecimal collectionAmount) {
		this.collectionAmount = collectionAmount;
	}

	public BigDecimal getRebateAmount() {
		return rebateAmount;
	}

	public void setRebateAmount(BigDecimal rebateAmount) {
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

	public Long getGlCode() {
		return glCode;
	}

	public void setGlCode(Long glCode) {
		this.glCode = glCode;
	}

	public Integer getIsActualDemand() {
		return isActualDemand;
	}

	public void setIsActualDemand(Integer isActualDemand) {
		this.isActualDemand = isActualDemand;
	}

}
