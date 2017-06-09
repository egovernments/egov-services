package org.egov.models;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Boundary details for a property. &#39;revenueBoundary&#39; is granular level Revenue heirarchy boundary, &#39;locationBoundary&#39; is granular level Location heirarchy boundary, &#39;adminBoundary&#39; is granular level Administration heirarchy boundary.
 * Author: Narendra
 */


public class PropertyLocation   {
	
	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("revenueBoundary")
	private Boundary revenueBoundary = null;

	@JsonProperty("locationBoundary")
	private Boundary locationBoundary = null;

	@JsonProperty("adminBoundary")
	private Boundary adminBoundary = null;

	@JsonProperty("northBoundedBy")
	@Size(min=1,max=256)
	private String northBoundedBy = null;

	@JsonProperty("eastBoundedBy")
	@Size(min=1,max=256)
	private String eastBoundedBy = null;

	@JsonProperty("westBoundedBy")
	@Size(min=1,max=256)
	private String westBoundedBy = null;

	@JsonProperty("southBoundedBy")
	@Size(min=1,max=256)
	private String southBoundedBy = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;


	/**
	 * Description : This is to get the searchType fields of the target class
	 * @author Narendra
	 * @param target
	 * @param searchType
	 * @return result
	 */
	public List<String> getFieldsOfType(Class<?> target, Class<?> searchType) {

		Field[] fields  = target.getDeclaredFields();
		List<String> result = new ArrayList<String>();

		for(Field field:fields){
			if(field.getType().equals(searchType)){
				result.add(searchType.getName());
			}
		}
		return result;
	}

	/**
	 * Description : This is to get the BoundaryType fields of the PropertyLocation class
	 * @author Narendra
	 * @return List<String>
	 */
	public List<String> getAllBoundaries(){
		return getFieldsOfType(PropertyLocation.class, Boundary.class); 
	}

	public PropertyLocation(Long id, Boundary revenueBoundary, Boundary locationBoundary, Boundary adminBoundary,
			String northBoundedBy, String eastBoundedBy, String westBoundedBy, String southBoundedBy,
			AuditDetails auditDetails) {
		super();
		this.id = id;
		this.revenueBoundary = revenueBoundary;
		this.locationBoundary = locationBoundary;
		this.adminBoundary = adminBoundary;
		this.northBoundedBy = northBoundedBy;
		this.eastBoundedBy = eastBoundedBy;
		this.westBoundedBy = westBoundedBy;
		this.southBoundedBy = southBoundedBy;
		this.auditDetails = auditDetails;
	}

	public PropertyLocation() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boundary getRevenueBoundary() {
		return revenueBoundary;
	}

	public void setRevenueBoundary(Boundary revenueBoundary) {
		this.revenueBoundary = revenueBoundary;
	}

	public Boundary getLocationBoundary() {
		return locationBoundary;
	}

	public void setLocationBoundary(Boundary locationBoundary) {
		this.locationBoundary = locationBoundary;
	}

	public Boundary getAdminBoundary() {
		return adminBoundary;
	}

	public void setAdminBoundary(Boundary adminBoundary) {
		this.adminBoundary = adminBoundary;
	}

	public String getNorthBoundedBy() {
		return northBoundedBy;
	}

	public void setNorthBoundedBy(String northBoundedBy) {
		this.northBoundedBy = northBoundedBy;
	}

	public String getEastBoundedBy() {
		return eastBoundedBy;
	}

	public void setEastBoundedBy(String eastBoundedBy) {
		this.eastBoundedBy = eastBoundedBy;
	}

	public String getWestBoundedBy() {
		return westBoundedBy;
	}

	public void setWestBoundedBy(String westBoundedBy) {
		this.westBoundedBy = westBoundedBy;
	}

	public String getSouthBoundedBy() {
		return southBoundedBy;
	}

	public void setSouthBoundedBy(String southBoundedBy) {
		this.southBoundedBy = southBoundedBy;
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
		result = prime * result + ((adminBoundary == null) ? 0 : adminBoundary.hashCode());
		result = prime * result + ((auditDetails == null) ? 0 : auditDetails.hashCode());
		result = prime * result + ((eastBoundedBy == null) ? 0 : eastBoundedBy.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((locationBoundary == null) ? 0 : locationBoundary.hashCode());
		result = prime * result + ((northBoundedBy == null) ? 0 : northBoundedBy.hashCode());
		result = prime * result + ((revenueBoundary == null) ? 0 : revenueBoundary.hashCode());
		result = prime * result + ((southBoundedBy == null) ? 0 : southBoundedBy.hashCode());
		result = prime * result + ((westBoundedBy == null) ? 0 : westBoundedBy.hashCode());
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
		PropertyLocation other = (PropertyLocation) obj;
		if (adminBoundary == null) {
			if (other.adminBoundary != null)
				return false;
		} else if (!adminBoundary.equals(other.adminBoundary))
			return false;
		if (auditDetails == null) {
			if (other.auditDetails != null)
				return false;
		} else if (!auditDetails.equals(other.auditDetails))
			return false;
		if (eastBoundedBy == null) {
			if (other.eastBoundedBy != null)
				return false;
		} else if (!eastBoundedBy.equals(other.eastBoundedBy))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (locationBoundary == null) {
			if (other.locationBoundary != null)
				return false;
		} else if (!locationBoundary.equals(other.locationBoundary))
			return false;
		if (northBoundedBy == null) {
			if (other.northBoundedBy != null)
				return false;
		} else if (!northBoundedBy.equals(other.northBoundedBy))
			return false;
		if (revenueBoundary == null) {
			if (other.revenueBoundary != null)
				return false;
		} else if (!revenueBoundary.equals(other.revenueBoundary))
			return false;
		if (southBoundedBy == null) {
			if (other.southBoundedBy != null)
				return false;
		} else if (!southBoundedBy.equals(other.southBoundedBy))
			return false;
		if (westBoundedBy == null) {
			if (other.westBoundedBy != null)
				return false;
		} else if (!westBoundedBy.equals(other.westBoundedBy))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PropertyLocation [id=" + id + ", revenueBoundary=" + revenueBoundary + ", locationBoundary="
				+ locationBoundary + ", adminBoundary=" + adminBoundary + ", northBoundedBy=" + northBoundedBy
				+ ", eastBoundedBy=" + eastBoundedBy + ", westBoundedBy=" + westBoundedBy + ", southBoundedBy="
				+ southBoundedBy + ", auditDetails=" + auditDetails + ", getAllBoundaries()=" + getAllBoundaries()
				+ ", getId()=" + getId() + ", getRevenueBoundary()=" + getRevenueBoundary() + ", getLocationBoundary()="
				+ getLocationBoundary() + ", getAdminBoundary()=" + getAdminBoundary() + ", getNorthBoundedBy()="
				+ getNorthBoundedBy() + ", getEastBoundedBy()=" + getEastBoundedBy() + ", getWestBoundedBy()="
				+ getWestBoundedBy() + ", getSouthBoundedBy()=" + getSouthBoundedBy() + ", getAuditDetails()="
				+ getAuditDetails() + ", hashCode()=" + hashCode() + ", getClass()=" + getClass() + ", toString()="
				+ super.toString() + "]";
	}
	
	

}

