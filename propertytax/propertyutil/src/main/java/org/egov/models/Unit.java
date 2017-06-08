package org.egov.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * A logical group of rooms on a floor
 * Author : Narendra
 */

public class Unit   {

	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("unitNo")
	@NotNull
	private Integer unitNo = null;

	/**
	 * type of the unit
	 */
	public enum UnitTypeEnum {
		FLAT("FLAT"),

		ROOM("ROOM");

		private String value;

		UnitTypeEnum(String value) {
			this.value = value;
		}

		@Override
		@JsonValue
		public String toString() {
			return String.valueOf(value);
		}

		@JsonCreator
		public static UnitTypeEnum fromValue(String text) {
			for (UnitTypeEnum b : UnitTypeEnum.values()) {
				if (String.valueOf(b.value).equals(text)) {
					return b;
				}
			}
			return null;
		}
	}

	@JsonProperty("unitType")
	@Size(min=1,max=16)
	private UnitTypeEnum unitType = null;

	@JsonProperty("length")
	private Double length = null;

	@JsonProperty("width")
	private Double width = null;

	@JsonProperty("builtupArea")
	@NotNull
	private Double builtupArea = null;

	@JsonProperty("assessableArea")
	private Double assessableArea = null;

	@JsonProperty("bpaBuiltupArea")
	private Double bpaBuiltupArea = null;

	@JsonProperty("bpaNo")
	@Size(min=1,max=16)
	private String bpaNo = null;

	@JsonProperty("bpaDate")
	private String bpaDate = null;

	@JsonProperty("usage")
	@NotNull
	@Size(min=1,max=16)
	private String usage = null;

	@JsonProperty("occupancy")
	@NotNull
	@Size(min=1,max=16)
	private String occupancy = null;

	@JsonProperty("occupierName")
	@Size(min=1,max=128)
	private String occupierName = null;

	@JsonProperty("firmName")
	@Size(min=1,max=128)
	private String firmName = null;

	@JsonProperty("rentCollected")
	private Double rentCollected = null;

	@JsonProperty("structure")
	@NotNull
	@Size(min=1,max=16)
	private String structure = null;

	@JsonProperty("age")
	@Size(min=1,max=16)
	private String age = null;

	@JsonProperty("exemptionReason")
	@Size(min=1,max=32)
	private String exemptionReason = null;

	@JsonProperty("isStructured")
	private Boolean isStructured = true;

	@JsonProperty("occupancyDate")
	private String occupancyDate = null;

	@JsonProperty("constCompletionDate")
	private String constCompletionDate = null;

	@JsonProperty("manualArv")
	private Double manualArv = null;

	@JsonProperty("arv")
	private Double arv = null;

	@JsonProperty("electricMeterNo")
	@Size(min=1,max=64)
	private String electricMeterNo = null;

	@JsonProperty("waterMeterNo")
	@Size(min=1,max=64)
	private String waterMeterNo = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

	public Unit(Long id, Integer unitNo, UnitTypeEnum unitType, Double length, Double width, Double builtupArea,
			Double assessableArea, Double bpaBuiltupArea, String bpaNo, String bpaDate, String usage, String occupancy,
			String occupierName, String firmName, Double rentCollected, String structure, String age,
			String exemptionReason, Boolean isStructured, String occupancyDate, String constCompletionDate,
			Double manualArv, Double arv, String electricMeterNo, String waterMeterNo, AuditDetails auditDetails) {
		super();
		this.id = id;
		this.unitNo = unitNo;
		this.unitType = unitType;
		this.length = length;
		this.width = width;
		this.builtupArea = builtupArea;
		this.assessableArea = assessableArea;
		this.bpaBuiltupArea = bpaBuiltupArea;
		this.bpaNo = bpaNo;
		this.bpaDate = bpaDate;
		this.usage = usage;
		this.occupancy = occupancy;
		this.occupierName = occupierName;
		this.firmName = firmName;
		this.rentCollected = rentCollected;
		this.structure = structure;
		this.age = age;
		this.exemptionReason = exemptionReason;
		this.isStructured = isStructured;
		this.occupancyDate = occupancyDate;
		this.constCompletionDate = constCompletionDate;
		this.manualArv = manualArv;
		this.arv = arv;
		this.electricMeterNo = electricMeterNo;
		this.waterMeterNo = waterMeterNo;
		this.auditDetails = auditDetails;
	}

	public Unit() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getUnitNo() {
		return unitNo;
	}

	public void setUnitNo(Integer unitNo) {
		this.unitNo = unitNo;
	}

	public UnitTypeEnum getUnitType() {
		return unitType;
	}

	public void setUnitType(UnitTypeEnum unitType) {
		this.unitType = unitType;
	}

	public Double getLength() {
		return length;
	}

	public void setLength(Double length) {
		this.length = length;
	}

	public Double getWidth() {
		return width;
	}

	public void setWidth(Double width) {
		this.width = width;
	}

	public Double getBuiltupArea() {
		return builtupArea;
	}

	public void setBuiltupArea(Double builtupArea) {
		this.builtupArea = builtupArea;
	}

	public Double getAssessableArea() {
		return assessableArea;
	}

	public void setAssessableArea(Double assessableArea) {
		this.assessableArea = assessableArea;
	}

	public Double getBpaBuiltupArea() {
		return bpaBuiltupArea;
	}

	public void setBpaBuiltupArea(Double bpaBuiltupArea) {
		this.bpaBuiltupArea = bpaBuiltupArea;
	}

	public String getBpaNo() {
		return bpaNo;
	}

	public void setBpaNo(String bpaNo) {
		this.bpaNo = bpaNo;
	}

	public String getBpaDate() {
		return bpaDate;
	}

	public void setBpaDate(String bpaDate) {
		this.bpaDate = bpaDate;
	}

	public String getUsage() {
		return usage;
	}

	public void setUsage(String usage) {
		this.usage = usage;
	}

	public String getOccupancy() {
		return occupancy;
	}

	public void setOccupancy(String occupancy) {
		this.occupancy = occupancy;
	}

	public String getOccupierName() {
		return occupierName;
	}

	public void setOccupierName(String occupierName) {
		this.occupierName = occupierName;
	}

	public String getFirmName() {
		return firmName;
	}

	public void setFirmName(String firmName) {
		this.firmName = firmName;
	}

	public Double getRentCollected() {
		return rentCollected;
	}

	public void setRentCollected(Double rentCollected) {
		this.rentCollected = rentCollected;
	}

	public String getStructure() {
		return structure;
	}

	public void setStructure(String structure) {
		this.structure = structure;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getExemptionReason() {
		return exemptionReason;
	}

	public void setExemptionReason(String exemptionReason) {
		this.exemptionReason = exemptionReason;
	}

	public Boolean getIsStructured() {
		return isStructured;
	}

	public void setIsStructured(Boolean isStructured) {
		this.isStructured = isStructured;
	}

	public String getOccupancyDate() {
		return occupancyDate;
	}

	public void setOccupancyDate(String occupancyDate) {
		this.occupancyDate = occupancyDate;
	}

	public String getConstCompletionDate() {
		return constCompletionDate;
	}

	public void setConstCompletionDate(String constCompletionDate) {
		this.constCompletionDate = constCompletionDate;
	}

	public Double getManualArv() {
		return manualArv;
	}

	public void setManualArv(Double manualArv) {
		this.manualArv = manualArv;
	}

	public Double getArv() {
		return arv;
	}

	public void setArv(Double arv) {
		this.arv = arv;
	}

	public String getElectricMeterNo() {
		return electricMeterNo;
	}

	public void setElectricMeterNo(String electricMeterNo) {
		this.electricMeterNo = electricMeterNo;
	}

	public String getWaterMeterNo() {
		return waterMeterNo;
	}

	public void setWaterMeterNo(String waterMeterNo) {
		this.waterMeterNo = waterMeterNo;
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
		result = prime * result + ((age == null) ? 0 : age.hashCode());
		result = prime * result + ((arv == null) ? 0 : arv.hashCode());
		result = prime * result + ((assessableArea == null) ? 0 : assessableArea.hashCode());
		result = prime * result + ((auditDetails == null) ? 0 : auditDetails.hashCode());
		result = prime * result + ((bpaBuiltupArea == null) ? 0 : bpaBuiltupArea.hashCode());
		result = prime * result + ((bpaDate == null) ? 0 : bpaDate.hashCode());
		result = prime * result + ((bpaNo == null) ? 0 : bpaNo.hashCode());
		result = prime * result + ((builtupArea == null) ? 0 : builtupArea.hashCode());
		result = prime * result + ((constCompletionDate == null) ? 0 : constCompletionDate.hashCode());
		result = prime * result + ((electricMeterNo == null) ? 0 : electricMeterNo.hashCode());
		result = prime * result + ((exemptionReason == null) ? 0 : exemptionReason.hashCode());
		result = prime * result + ((firmName == null) ? 0 : firmName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((isStructured == null) ? 0 : isStructured.hashCode());
		result = prime * result + ((length == null) ? 0 : length.hashCode());
		result = prime * result + ((manualArv == null) ? 0 : manualArv.hashCode());
		result = prime * result + ((occupancy == null) ? 0 : occupancy.hashCode());
		result = prime * result + ((occupancyDate == null) ? 0 : occupancyDate.hashCode());
		result = prime * result + ((occupierName == null) ? 0 : occupierName.hashCode());
		result = prime * result + ((rentCollected == null) ? 0 : rentCollected.hashCode());
		result = prime * result + ((structure == null) ? 0 : structure.hashCode());
		result = prime * result + ((unitNo == null) ? 0 : unitNo.hashCode());
		result = prime * result + ((unitType == null) ? 0 : unitType.hashCode());
		result = prime * result + ((usage == null) ? 0 : usage.hashCode());
		result = prime * result + ((waterMeterNo == null) ? 0 : waterMeterNo.hashCode());
		result = prime * result + ((width == null) ? 0 : width.hashCode());
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
		Unit other = (Unit) obj;
		if (age == null) {
			if (other.age != null)
				return false;
		} else if (!age.equals(other.age))
			return false;
		if (arv == null) {
			if (other.arv != null)
				return false;
		} else if (!arv.equals(other.arv))
			return false;
		if (assessableArea == null) {
			if (other.assessableArea != null)
				return false;
		} else if (!assessableArea.equals(other.assessableArea))
			return false;
		if (auditDetails == null) {
			if (other.auditDetails != null)
				return false;
		} else if (!auditDetails.equals(other.auditDetails))
			return false;
		if (bpaBuiltupArea == null) {
			if (other.bpaBuiltupArea != null)
				return false;
		} else if (!bpaBuiltupArea.equals(other.bpaBuiltupArea))
			return false;
		if (bpaDate == null) {
			if (other.bpaDate != null)
				return false;
		} else if (!bpaDate.equals(other.bpaDate))
			return false;
		if (bpaNo == null) {
			if (other.bpaNo != null)
				return false;
		} else if (!bpaNo.equals(other.bpaNo))
			return false;
		if (builtupArea == null) {
			if (other.builtupArea != null)
				return false;
		} else if (!builtupArea.equals(other.builtupArea))
			return false;
		if (constCompletionDate == null) {
			if (other.constCompletionDate != null)
				return false;
		} else if (!constCompletionDate.equals(other.constCompletionDate))
			return false;
		if (electricMeterNo == null) {
			if (other.electricMeterNo != null)
				return false;
		} else if (!electricMeterNo.equals(other.electricMeterNo))
			return false;
		if (exemptionReason == null) {
			if (other.exemptionReason != null)
				return false;
		} else if (!exemptionReason.equals(other.exemptionReason))
			return false;
		if (firmName == null) {
			if (other.firmName != null)
				return false;
		} else if (!firmName.equals(other.firmName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (isStructured == null) {
			if (other.isStructured != null)
				return false;
		} else if (!isStructured.equals(other.isStructured))
			return false;
		if (length == null) {
			if (other.length != null)
				return false;
		} else if (!length.equals(other.length))
			return false;
		if (manualArv == null) {
			if (other.manualArv != null)
				return false;
		} else if (!manualArv.equals(other.manualArv))
			return false;
		if (occupancy == null) {
			if (other.occupancy != null)
				return false;
		} else if (!occupancy.equals(other.occupancy))
			return false;
		if (occupancyDate == null) {
			if (other.occupancyDate != null)
				return false;
		} else if (!occupancyDate.equals(other.occupancyDate))
			return false;
		if (occupierName == null) {
			if (other.occupierName != null)
				return false;
		} else if (!occupierName.equals(other.occupierName))
			return false;
		if (rentCollected == null) {
			if (other.rentCollected != null)
				return false;
		} else if (!rentCollected.equals(other.rentCollected))
			return false;
		if (structure == null) {
			if (other.structure != null)
				return false;
		} else if (!structure.equals(other.structure))
			return false;
		if (unitNo == null) {
			if (other.unitNo != null)
				return false;
		} else if (!unitNo.equals(other.unitNo))
			return false;
		if (unitType != other.unitType)
			return false;
		if (usage == null) {
			if (other.usage != null)
				return false;
		} else if (!usage.equals(other.usage))
			return false;
		if (waterMeterNo == null) {
			if (other.waterMeterNo != null)
				return false;
		} else if (!waterMeterNo.equals(other.waterMeterNo))
			return false;
		if (width == null) {
			if (other.width != null)
				return false;
		} else if (!width.equals(other.width))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Unit [id=" + id + ", unitNo=" + unitNo + ", unitType=" + unitType + ", length=" + length + ", width="
				+ width + ", builtupArea=" + builtupArea + ", assessableArea=" + assessableArea + ", bpaBuiltupArea="
				+ bpaBuiltupArea + ", bpaNo=" + bpaNo + ", bpaDate=" + bpaDate + ", usage=" + usage + ", occupancy="
				+ occupancy + ", occupierName=" + occupierName + ", firmName=" + firmName + ", rentCollected="
				+ rentCollected + ", structure=" + structure + ", age=" + age + ", exemptionReason=" + exemptionReason
				+ ", isStructured=" + isStructured + ", occupancyDate=" + occupancyDate + ", constCompletionDate="
				+ constCompletionDate + ", manualArv=" + manualArv + ", arv=" + arv + ", electricMeterNo="
				+ electricMeterNo + ", waterMeterNo=" + waterMeterNo + ", auditDetails=" + auditDetails + "]";
	}



}

