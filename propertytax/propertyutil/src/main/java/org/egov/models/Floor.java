package org.egov.models;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * The floor details. The Floor contains either Flats in case of apartment or Rooms in case of group or individual house.
 */

public class Floor   {
	
	@JsonProperty("id")
	private Long id = null;
	
	@JsonProperty("floorNo")
	@NotNull
	@Size(min=1,max=16)
	private String floorNo = null;

	@JsonProperty("units")
	@Valid
	private List<Unit> units = new ArrayList<Unit>();


	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;


	public Floor(Long id, String floorNo, List<Unit> units, AuditDetails auditDetails) {
		super();
		this.id = id;
		this.floorNo = floorNo;
		this.units = units;
		this.auditDetails = auditDetails;
	}


	public Floor() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getFloorNo() {
		return floorNo;
	}


	public void setFloorNo(String floorNo) {
		this.floorNo = floorNo;
	}


	public List<Unit> getUnits() {
		return units;
	}


	public void setUnits(List<Unit> units) {
		this.units = units;
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
		result = prime * result + ((auditDetails == null) ? 0 : auditDetails.hashCode());
		result = prime * result + ((floorNo == null) ? 0 : floorNo.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((units == null) ? 0 : units.hashCode());
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
		Floor other = (Floor) obj;
		if (auditDetails == null) {
			if (other.auditDetails != null)
				return false;
		} else if (!auditDetails.equals(other.auditDetails))
			return false;
		if (floorNo == null) {
			if (other.floorNo != null)
				return false;
		} else if (!floorNo.equals(other.floorNo))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (units == null) {
			if (other.units != null)
				return false;
		} else if (!units.equals(other.units))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "Floor [id=" + id + ", floorNo=" + floorNo + ", units=" + units + ", auditDetails=" + auditDetails
				+ ", getId()=" + getId() + ", getFloorNo()=" + getFloorNo() + ", getUnits()=" + getUnits()
				+ ", getAuditDetails()=" + getAuditDetails() + ", hashCode()=" + hashCode() + ", getClass()="
				+ getClass() + ", toString()=" + super.toString() + "]";
	}


}

