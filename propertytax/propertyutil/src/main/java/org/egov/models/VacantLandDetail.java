package org.egov.models;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * VacantLandDetail
 * Author : Narendra
 */
public class VacantLandDetail   {
	
	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("surveyNumber")
	@Size(min=4,max=64)
	private String surveyNumber = null;

	@JsonProperty("pattaNumber")
	@Size(min=4,max=64)
	private String pattaNumber = null;

	@JsonProperty("marketValue")
	private Double marketValue = null;

	@JsonProperty("capitalValue")
	private Double capitalValue = null;

	@JsonProperty("layoutApprovedAuth")
	@Size(min=4,max=64)
	private String layoutApprovedAuth = null;

	@JsonProperty("layoutPermissionNo")
	@Size(min=4,max=64)
	private String layoutPermissionNo = null;

	@JsonProperty("layoutPermissionDate")
	private String layoutPermissionDate = null;

	@JsonProperty("resdPlotArea")
	private Double resdPlotArea = null;

	@JsonProperty("nonResdPlotArea")
	private Double nonResdPlotArea = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

	public VacantLandDetail(Long id, String surveyNumber, String pattaNumber, Double marketValue, Double capitalValue,
			String layoutApprovedAuth, String layoutPermissionNo, String layoutPermissionDate, Double resdPlotArea,
			Double nonResdPlotArea, AuditDetails auditDetails) {
		super();
		this.id = id;
		this.surveyNumber = surveyNumber;
		this.pattaNumber = pattaNumber;
		this.marketValue = marketValue;
		this.capitalValue = capitalValue;
		this.layoutApprovedAuth = layoutApprovedAuth;
		this.layoutPermissionNo = layoutPermissionNo;
		this.layoutPermissionDate = layoutPermissionDate;
		this.resdPlotArea = resdPlotArea;
		this.nonResdPlotArea = nonResdPlotArea;
		this.auditDetails = auditDetails;
	}

	public VacantLandDetail() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((auditDetails == null) ? 0 : auditDetails.hashCode());
		result = prime * result + ((capitalValue == null) ? 0 : capitalValue.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((layoutApprovedAuth == null) ? 0 : layoutApprovedAuth.hashCode());
		result = prime * result + ((layoutPermissionDate == null) ? 0 : layoutPermissionDate.hashCode());
		result = prime * result + ((layoutPermissionNo == null) ? 0 : layoutPermissionNo.hashCode());
		result = prime * result + ((marketValue == null) ? 0 : marketValue.hashCode());
		result = prime * result + ((nonResdPlotArea == null) ? 0 : nonResdPlotArea.hashCode());
		result = prime * result + ((pattaNumber == null) ? 0 : pattaNumber.hashCode());
		result = prime * result + ((resdPlotArea == null) ? 0 : resdPlotArea.hashCode());
		result = prime * result + ((surveyNumber == null) ? 0 : surveyNumber.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VacantLandDetail other = (VacantLandDetail) obj;
		if (auditDetails == null) {
			if (other.auditDetails != null)
				return false;
		} else if (!auditDetails.equals(other.auditDetails))
			return false;
		if (capitalValue == null) {
			if (other.capitalValue != null)
				return false;
		} else if (!capitalValue.equals(other.capitalValue))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (layoutApprovedAuth == null) {
			if (other.layoutApprovedAuth != null)
				return false;
		} else if (!layoutApprovedAuth.equals(other.layoutApprovedAuth))
			return false;
		if (layoutPermissionDate == null) {
			if (other.layoutPermissionDate != null)
				return false;
		} else if (!layoutPermissionDate.equals(other.layoutPermissionDate))
			return false;
		if (layoutPermissionNo == null) {
			if (other.layoutPermissionNo != null)
				return false;
		} else if (!layoutPermissionNo.equals(other.layoutPermissionNo))
			return false;
		if (marketValue == null) {
			if (other.marketValue != null)
				return false;
		} else if (!marketValue.equals(other.marketValue))
			return false;
		if (nonResdPlotArea == null) {
			if (other.nonResdPlotArea != null)
				return false;
		} else if (!nonResdPlotArea.equals(other.nonResdPlotArea))
			return false;
		if (pattaNumber == null) {
			if (other.pattaNumber != null)
				return false;
		} else if (!pattaNumber.equals(other.pattaNumber))
			return false;
		if (resdPlotArea == null) {
			if (other.resdPlotArea != null)
				return false;
		} else if (!resdPlotArea.equals(other.resdPlotArea))
			return false;
		if (surveyNumber == null) {
			if (other.surveyNumber != null)
				return false;
		} else if (!surveyNumber.equals(other.surveyNumber))
			return false;
		return true;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSurveyNumber() {
		return surveyNumber;
	}

	public void setSurveyNumber(String surveyNumber) {
		this.surveyNumber = surveyNumber;
	}

	public String getPattaNumber() {
		return pattaNumber;
	}

	public void setPattaNumber(String pattaNumber) {
		this.pattaNumber = pattaNumber;
	}

	public Double getMarketValue() {
		return marketValue;
	}

	public void setMarketValue(Double marketValue) {
		this.marketValue = marketValue;
	}

	public Double getCapitalValue() {
		return capitalValue;
	}

	public void setCapitalValue(Double capitalValue) {
		this.capitalValue = capitalValue;
	}

	public String getLayoutApprovedAuth() {
		return layoutApprovedAuth;
	}

	public void setLayoutApprovedAuth(String layoutApprovedAuth) {
		this.layoutApprovedAuth = layoutApprovedAuth;
	}

	public String getLayoutPermissionNo() {
		return layoutPermissionNo;
	}

	public void setLayoutPermissionNo(String layoutPermissionNo) {
		this.layoutPermissionNo = layoutPermissionNo;
	}

	public String getLayoutPermissionDate() {
		return layoutPermissionDate;
	}

	public void setLayoutPermissionDate(String layoutPermissionDate) {
		this.layoutPermissionDate = layoutPermissionDate;
	}

	public Double getResdPlotArea() {
		return resdPlotArea;
	}

	public void setResdPlotArea(Double resdPlotArea) {
		this.resdPlotArea = resdPlotArea;
	}

	public Double getNonResdPlotArea() {
		return nonResdPlotArea;
	}

	public void setNonResdPlotArea(Double nonResdPlotArea) {
		this.nonResdPlotArea = nonResdPlotArea;
	}

	public AuditDetails getAuditDetails() {
		return auditDetails;
	}

	public void setAuditDetails(AuditDetails auditDetails) {
		this.auditDetails = auditDetails;
	}

	@Override
	public String toString() {
		return "VacantLandDetail [id=" + id + ", surveyNumber=" + surveyNumber + ", pattaNumber=" + pattaNumber
				+ ", marketValue=" + marketValue + ", capitalValue=" + capitalValue + ", layoutApprovedAuth="
				+ layoutApprovedAuth + ", layoutPermissionNo=" + layoutPermissionNo + ", layoutPermissionDate="
				+ layoutPermissionDate + ", resdPlotArea=" + resdPlotArea + ", nonResdPlotArea=" + nonResdPlotArea
				+ ", auditDetails=" + auditDetails + "]";
	}


}

