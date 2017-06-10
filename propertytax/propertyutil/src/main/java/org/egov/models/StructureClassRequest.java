package org.egov.models;

import java.util.List;

public class StructureClassRequest {
	private RequestInfo requestInfo;

	private List<StructureClass> structureClasses;

	public RequestInfo getRequestInfo() {
		return requestInfo;
	}

	public void setRequestInfo(RequestInfo requestInfo) {
		this.requestInfo = requestInfo;
	}

	public List<StructureClass> getStructureClasses() {
		return structureClasses;
	}

	public void setStructureClasses(List<StructureClass> structureClasses) {
		this.structureClasses = structureClasses;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((requestInfo == null) ? 0 : requestInfo.hashCode());
		result = prime * result + ((structureClasses == null) ? 0 : structureClasses.hashCode());
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
		StructureClassRequest other = (StructureClassRequest) obj;
		if (requestInfo == null) {
			if (other.requestInfo != null)
				return false;
		} else if (!requestInfo.equals(other.requestInfo))
			return false;
		if (structureClasses == null) {
			if (other.structureClasses != null)
				return false;
		} else if (!structureClasses.equals(other.structureClasses))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "StructureClassRequest [requestInfo=" + requestInfo + ", structureClasses=" + structureClasses
				+ ", getRequestInfo()=" + getRequestInfo() + ", getStructureClasses()=" + getStructureClasses()
				+ ", hashCode()=" + hashCode() + ", getClass()=" + getClass() + ", toString()=" + super.toString()
				+ "]";
	}

	public StructureClassRequest(RequestInfo requestInfo, List<StructureClass> structureClasses) {
		super();
		this.requestInfo = requestInfo;
		this.structureClasses = structureClasses;
	}

	public StructureClassRequest() {
		super();
		// TODO Auto-generated constructor stub
	}


}
