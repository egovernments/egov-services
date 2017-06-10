package org.egov.models;

import java.util.List;

public class PropertyTypeRequest {
	private RequestInfo requestInfo;

	private List<PropertyType> propertyTypes;

	public RequestInfo getRequestInfo() {
		return requestInfo;
	}

	public void setRequestInfo(RequestInfo requestInfo) {
		this.requestInfo = requestInfo;
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
		result = prime * result + ((requestInfo == null) ? 0 : requestInfo.hashCode());
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
		PropertyTypeRequest other = (PropertyTypeRequest) obj;
		if (propertyTypes == null) {
			if (other.propertyTypes != null)
				return false;
		} else if (!propertyTypes.equals(other.propertyTypes))
			return false;
		if (requestInfo == null) {
			if (other.requestInfo != null)
				return false;
		} else if (!requestInfo.equals(other.requestInfo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PropertyTypeRequest [requestInfo=" + requestInfo + ", propertyTypes=" + propertyTypes
				+ ", getRequestInfo()=" + getRequestInfo() + ", getPropertyTypes()=" + getPropertyTypes()
				+ ", hashCode()=" + hashCode() + ", getClass()=" + getClass() + ", toString()=" + super.toString()
				+ "]";
	}

	public PropertyTypeRequest(RequestInfo requestInfo, List<PropertyType> propertyTypes) {
		super();
		this.requestInfo = requestInfo;
		this.propertyTypes = propertyTypes;
	}

	public PropertyTypeRequest() {
		super();
		// TODO Auto-generated constructor stub
	}


}
