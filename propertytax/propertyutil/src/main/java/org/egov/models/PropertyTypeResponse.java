package org.egov.models;

import java.util.List;

public class PropertyTypeResponse {
	private ResponseInfo responseInfo;

	private List<PropertyType> propertyTypes;

	public ResponseInfo getResponseInfo() {
		return responseInfo;
	}

	public void setResponseInfo(ResponseInfo responseInfo) {
		this.responseInfo = responseInfo;
	}

	public List<PropertyType> getPropertyTypes() {
		return propertyTypes;
	}

	public void setPropertyTypes(List<PropertyType> propertyTypes) {
		this.propertyTypes = propertyTypes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((propertyTypes == null) ? 0 : propertyTypes.hashCode());
		result = prime * result + ((responseInfo == null) ? 0 : responseInfo.hashCode());
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
		PropertyTypeResponse other = (PropertyTypeResponse) obj;
		if (propertyTypes == null) {
			if (other.propertyTypes != null)
				return false;
		} else if (!propertyTypes.equals(other.propertyTypes))
			return false;
		if (responseInfo == null) {
			if (other.responseInfo != null)
				return false;
		} else if (!responseInfo.equals(other.responseInfo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PropertyTypeResponse [responseInfo=" + responseInfo + ", propertyTypes=" + propertyTypes
				+ ", getResponseInfo()=" + getResponseInfo() + ", getPropertyTypes()=" + getPropertyTypes()
				+ ", hashCode()=" + hashCode() + ", getClass()=" + getClass() + ", toString()=" + super.toString()
				+ "]";
	}

	public PropertyTypeResponse(ResponseInfo responseInfo, List<PropertyType> propertyTypes) {
		super();
		this.responseInfo = responseInfo;
		this.propertyTypes = propertyTypes;
	}

	public PropertyTypeResponse() {
		super();
		// TODO Auto-generated constructor stub
	}


}
