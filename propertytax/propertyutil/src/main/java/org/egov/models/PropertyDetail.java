package org.egov.models;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * PropertyDetail
 * Author : narendra
 */

public class PropertyDetail   {

	@JsonProperty("id")
	private Long id = null;

	/**
	 * Source of a assessment data. The properties will be created in a system based on the data avaialble in their manual records or during field survey. There can be more from client to client.
	 */
	public enum SourceEnum {
		MUNICIPAL_RECORDS("MUNICIPAL_RECORDS"),

		FIELD_SURVEY("FIELD_SURVEY");

		private String value;

		SourceEnum(String value) {
			this.value = value;
		}

		@Override
		@JsonValue
		public String toString() {
			return String.valueOf(value);
		}

		@JsonCreator
		public static SourceEnum fromValue(String text) {
			for (SourceEnum b : SourceEnum.values()) {
				if (String.valueOf(b.value).equals(text)) {
					return b;
				}
			}
			return null;
		}
	}

	@JsonProperty("source")
	@Size(min=1,max=64)
	private SourceEnum source = null;

	@JsonProperty("regdDocNo")
	@Size(min=1,max=64)
	private String regdDocNo = null;

	@JsonProperty("regdDocDate")
	private String regdDocDate = null;

	@JsonProperty("reason")
	@Size(min=1,max=16)
	private String reason = null;

	/**
	 * status of the Property
	 */
	public enum StatusEnum {
		ACTIVE("ACTIVE"),

		INACTIVE("INACTIVE"),

		WORKFLOW("WORKFLOW"),

		HISTORY("HISTORY"),

		CANCELED("CANCELED");

		private String value;

		StatusEnum(String value) {
			this.value = value;
		}

		@Override
		@JsonValue
		public String toString() {
			return String.valueOf(value);
		}

		@JsonCreator
		public static StatusEnum fromValue(String text) {
			for (StatusEnum b : StatusEnum.values()) {
				if (String.valueOf(b.value).equals(text)) {
					return b;
				}
			}
			return null;
		}
	}

	@JsonProperty("status")
	@Size(min=1,max=8)
	private StatusEnum status = null;

	@JsonProperty("isVerified")
	private Boolean isVerified = null;

	@JsonProperty("verificationDate")
	private String verificationDate = null;

	@JsonProperty("isExempted")
	private Boolean isExempted = false;

	@JsonProperty("exemptionReason")
	@Size(min=1,max=32)
	private String exemptionReason = null;

	@JsonProperty("propertyType")
	@NotNull
	@Size(min=1,max=16)
	private String propertyType = null;

	@JsonProperty("category")
	@Size(min=1,max=16)
	private String category = null;

	@JsonProperty("usage")
	@Size(min=1,max=16)
	private String usage = null;

	@JsonProperty("department")
	@Size(min=1,max=16)
	private String department = null;

	@JsonProperty("apartment")
	@Size(min=1,max=16)
	private String apartment = null;

	@JsonProperty("siteLength")
	private Double siteLength = null;

	@JsonProperty("siteBreadth")
	private Double siteBreadth = null;

	@JsonProperty("sitalArea")
	@NotNull
	private Double sitalArea = null;

	@JsonProperty("totalBuiltupArea")
	private Double totalBuiltupArea = null;

	@JsonProperty("undividedShare")
	private Double undividedShare = null;

	@JsonProperty("noOfFloors")
	private Long noOfFloors = null;

	@JsonProperty("isSuperStructure")
	private Boolean isSuperStructure = false;

	@JsonProperty("landOwner")
	@Size(min=1,max=128)
	private String landOwner = null;

	@JsonProperty("floorType")
	@Size(min=1,max=16)
	private String floorType = null;

	@JsonProperty("woodType")
	@Size(min=1,max=16)
	private String woodType = null;

	@JsonProperty("roofType")
	@Size(min=1,max=16)
	private String roofType = null;

	@JsonProperty("wallType")
	@Size(min=1,max=16)
	private String wallType = null;

	@JsonProperty("floors")
	private List<Floor> floors = new ArrayList<Floor>();

	@JsonProperty("documents")
	private List<Document> documents = new ArrayList<Document>();

	@JsonProperty("stateId")
	private String stateId = null;

	@JsonProperty("applicationNo")
	@Size(min=1,max=64)
	private String applicationNo = null;

	@JsonProperty("workFlowDetails")
	private WorkFlowDetails workFlowDetails = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SourceEnum getSource() {
		return source;
	}

	public void setSource(SourceEnum source) {
		this.source = source;
	}

	public String getRegdDocNo() {
		return regdDocNo;
	}

	public void setRegdDocNo(String regdDocNo) {
		this.regdDocNo = regdDocNo;
	}

	public String getRegdDocDate() {
		return regdDocDate;
	}

	public void setRegdDocDate(String regdDocDate) {
		this.regdDocDate = regdDocDate;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public StatusEnum getStatus() {
		return status;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}

	public Boolean getIsVerified() {
		return isVerified;
	}

	public void setIsVerified(Boolean isVerified) {
		this.isVerified = isVerified;
	}

	public String getVerificationDate() {
		return verificationDate;
	}

	public void setVerificationDate(String verificationDate) {
		this.verificationDate = verificationDate;
	}

	public Boolean getIsExempted() {
		return isExempted;
	}

	public void setIsExempted(Boolean isExempted) {
		this.isExempted = isExempted;
	}

	public String getExemptionReason() {
		return exemptionReason;
	}

	public void setExemptionReason(String exemptionReason) {
		this.exemptionReason = exemptionReason;
	}

	public String getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getUsage() {
		return usage;
	}

	public void setUsage(String usage) {
		this.usage = usage;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getApartment() {
		return apartment;
	}

	public void setApartment(String apartment) {
		this.apartment = apartment;
	}

	public Double getSiteLength() {
		return siteLength;
	}

	public void setSiteLength(Double siteLength) {
		this.siteLength = siteLength;
	}

	public Double getSiteBreadth() {
		return siteBreadth;
	}

	public void setSiteBreadth(Double siteBreadth) {
		this.siteBreadth = siteBreadth;
	}

	public Double getSitalArea() {
		return sitalArea;
	}

	public void setSitalArea(Double sitalArea) {
		this.sitalArea = sitalArea;
	}

	public Double getTotalBuiltupArea() {
		return totalBuiltupArea;
	}

	public void setTotalBuiltupArea(Double totalBuiltupArea) {
		this.totalBuiltupArea = totalBuiltupArea;
	}

	public Double getUndividedShare() {
		return undividedShare;
	}

	public void setUndividedShare(Double undividedShare) {
		this.undividedShare = undividedShare;
	}

	public Long getNoOfFloors() {
		return noOfFloors;
	}

	public void setNoOfFloors(Long noOfFloors) {
		this.noOfFloors = noOfFloors;
	}

	public Boolean getIsSuperStructure() {
		return isSuperStructure;
	}

	public void setIsSuperStructure(Boolean isSuperStructure) {
		this.isSuperStructure = isSuperStructure;
	}

	public String getLandOwner() {
		return landOwner;
	}

	public void setLandOwner(String landOwner) {
		this.landOwner = landOwner;
	}

	public String getFloorType() {
		return floorType;
	}

	public void setFloorType(String floorType) {
		this.floorType = floorType;
	}

	public String getWoodType() {
		return woodType;
	}

	public void setWoodType(String woodType) {
		this.woodType = woodType;
	}

	public String getRoofType() {
		return roofType;
	}

	public void setRoofType(String roofType) {
		this.roofType = roofType;
	}

	public String getWallType() {
		return wallType;
	}

	public void setWallType(String wallType) {
		this.wallType = wallType;
	}

	public List<Floor> getFloors() {
		return floors;
	}

	public void setFloors(List<Floor> floors) {
		this.floors = floors;
	}

	public List<Document> getDocuments() {
		return documents;
	}

	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}

	public String getStateId() {
		return stateId;
	}

	public void setStateId(String stateId) {
		this.stateId = stateId;
	}

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

	public WorkFlowDetails getWorkFlowDetails() {
		return workFlowDetails;
	}

	public void setWorkFlowDetails(WorkFlowDetails workFlowDetails) {
		this.workFlowDetails = workFlowDetails;
	}

	public AuditDetails getAuditDetails() {
		return auditDetails;
	}

	public void setAuditDetails(AuditDetails auditDetails) {
		this.auditDetails = auditDetails;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((apartment == null) ? 0 : apartment.hashCode());
		result = prime * result + ((applicationNo == null) ? 0 : applicationNo.hashCode());
		result = prime * result + ((auditDetails == null) ? 0 : auditDetails.hashCode());
		result = prime * result + ((category == null) ? 0 : category.hashCode());
		result = prime * result + ((department == null) ? 0 : department.hashCode());
		result = prime * result + ((documents == null) ? 0 : documents.hashCode());
		result = prime * result + ((exemptionReason == null) ? 0 : exemptionReason.hashCode());
		result = prime * result + ((floorType == null) ? 0 : floorType.hashCode());
		result = prime * result + ((floors == null) ? 0 : floors.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((isExempted == null) ? 0 : isExempted.hashCode());
		result = prime * result + ((isSuperStructure == null) ? 0 : isSuperStructure.hashCode());
		result = prime * result + ((isVerified == null) ? 0 : isVerified.hashCode());
		result = prime * result + ((landOwner == null) ? 0 : landOwner.hashCode());
		result = prime * result + ((noOfFloors == null) ? 0 : noOfFloors.hashCode());
		result = prime * result + ((propertyType == null) ? 0 : propertyType.hashCode());
		result = prime * result + ((reason == null) ? 0 : reason.hashCode());
		result = prime * result + ((regdDocDate == null) ? 0 : regdDocDate.hashCode());
		result = prime * result + ((regdDocNo == null) ? 0 : regdDocNo.hashCode());
		result = prime * result + ((roofType == null) ? 0 : roofType.hashCode());
		result = prime * result + ((sitalArea == null) ? 0 : sitalArea.hashCode());
		result = prime * result + ((siteBreadth == null) ? 0 : siteBreadth.hashCode());
		result = prime * result + ((siteLength == null) ? 0 : siteLength.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		result = prime * result + ((stateId == null) ? 0 : stateId.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((totalBuiltupArea == null) ? 0 : totalBuiltupArea.hashCode());
		result = prime * result + ((undividedShare == null) ? 0 : undividedShare.hashCode());
		result = prime * result + ((usage == null) ? 0 : usage.hashCode());
		result = prime * result + ((verificationDate == null) ? 0 : verificationDate.hashCode());
		result = prime * result + ((wallType == null) ? 0 : wallType.hashCode());
		result = prime * result + ((woodType == null) ? 0 : woodType.hashCode());
		result = prime * result + ((workFlowDetails == null) ? 0 : workFlowDetails.hashCode());
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
		PropertyDetail other = (PropertyDetail) obj;
		if (apartment == null) {
			if (other.apartment != null)
				return false;
		} else if (!apartment.equals(other.apartment))
			return false;
		if (applicationNo == null) {
			if (other.applicationNo != null)
				return false;
		} else if (!applicationNo.equals(other.applicationNo))
			return false;
		if (auditDetails == null) {
			if (other.auditDetails != null)
				return false;
		} else if (!auditDetails.equals(other.auditDetails))
			return false;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (department == null) {
			if (other.department != null)
				return false;
		} else if (!department.equals(other.department))
			return false;
		if (documents == null) {
			if (other.documents != null)
				return false;
		} else if (!documents.equals(other.documents))
			return false;
		if (exemptionReason == null) {
			if (other.exemptionReason != null)
				return false;
		} else if (!exemptionReason.equals(other.exemptionReason))
			return false;
		if (floorType == null) {
			if (other.floorType != null)
				return false;
		} else if (!floorType.equals(other.floorType))
			return false;
		if (floors == null) {
			if (other.floors != null)
				return false;
		} else if (!floors.equals(other.floors))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (isExempted == null) {
			if (other.isExempted != null)
				return false;
		} else if (!isExempted.equals(other.isExempted))
			return false;
		if (isSuperStructure == null) {
			if (other.isSuperStructure != null)
				return false;
		} else if (!isSuperStructure.equals(other.isSuperStructure))
			return false;
		if (isVerified == null) {
			if (other.isVerified != null)
				return false;
		} else if (!isVerified.equals(other.isVerified))
			return false;
		if (landOwner == null) {
			if (other.landOwner != null)
				return false;
		} else if (!landOwner.equals(other.landOwner))
			return false;
		if (noOfFloors == null) {
			if (other.noOfFloors != null)
				return false;
		} else if (!noOfFloors.equals(other.noOfFloors))
			return false;
		if (propertyType == null) {
			if (other.propertyType != null)
				return false;
		} else if (!propertyType.equals(other.propertyType))
			return false;
		if (reason == null) {
			if (other.reason != null)
				return false;
		} else if (!reason.equals(other.reason))
			return false;
		if (regdDocDate == null) {
			if (other.regdDocDate != null)
				return false;
		} else if (!regdDocDate.equals(other.regdDocDate))
			return false;
		if (regdDocNo == null) {
			if (other.regdDocNo != null)
				return false;
		} else if (!regdDocNo.equals(other.regdDocNo))
			return false;
		if (roofType == null) {
			if (other.roofType != null)
				return false;
		} else if (!roofType.equals(other.roofType))
			return false;
		if (sitalArea == null) {
			if (other.sitalArea != null)
				return false;
		} else if (!sitalArea.equals(other.sitalArea))
			return false;
		if (siteBreadth == null) {
			if (other.siteBreadth != null)
				return false;
		} else if (!siteBreadth.equals(other.siteBreadth))
			return false;
		if (siteLength == null) {
			if (other.siteLength != null)
				return false;
		} else if (!siteLength.equals(other.siteLength))
			return false;
		if (source != other.source)
			return false;
		if (stateId == null) {
			if (other.stateId != null)
				return false;
		} else if (!stateId.equals(other.stateId))
			return false;
		if (status != other.status)
			return false;
		if (totalBuiltupArea == null) {
			if (other.totalBuiltupArea != null)
				return false;
		} else if (!totalBuiltupArea.equals(other.totalBuiltupArea))
			return false;
		if (undividedShare == null) {
			if (other.undividedShare != null)
				return false;
		} else if (!undividedShare.equals(other.undividedShare))
			return false;
		if (usage == null) {
			if (other.usage != null)
				return false;
		} else if (!usage.equals(other.usage))
			return false;
		if (verificationDate == null) {
			if (other.verificationDate != null)
				return false;
		} else if (!verificationDate.equals(other.verificationDate))
			return false;
		if (wallType == null) {
			if (other.wallType != null)
				return false;
		} else if (!wallType.equals(other.wallType))
			return false;
		if (woodType == null) {
			if (other.woodType != null)
				return false;
		} else if (!woodType.equals(other.woodType))
			return false;
		if (workFlowDetails == null) {
			if (other.workFlowDetails != null)
				return false;
		} else if (!workFlowDetails.equals(other.workFlowDetails))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PropertyDetail [id=" + id + ", source=" + source + ", regdDocNo=" + regdDocNo + ", regdDocDate="
				+ regdDocDate + ", reason=" + reason + ", status=" + status + ", isVerified=" + isVerified
				+ ", verificationDate=" + verificationDate + ", isExempted=" + isExempted + ", exemptionReason="
				+ exemptionReason + ", propertyType=" + propertyType + ", category=" + category + ", usage=" + usage
				+ ", department=" + department + ", apartment=" + apartment + ", siteLength=" + siteLength
				+ ", siteBreadth=" + siteBreadth + ", sitalArea=" + sitalArea + ", totalBuiltupArea=" + totalBuiltupArea
				+ ", undividedShare=" + undividedShare + ", noOfFloors=" + noOfFloors + ", isSuperStructure="
				+ isSuperStructure + ", landOwner=" + landOwner + ", floorType=" + floorType + ", woodType=" + woodType
				+ ", roofType=" + roofType + ", wallType=" + wallType + ", floors=" + floors + ", documents="
				+ documents + ", stateId=" + stateId + ", applicationNo=" + applicationNo + ", workFlowDetails="
				+ workFlowDetails + ", auditDetails=" + auditDetails + "]";
	}
}

