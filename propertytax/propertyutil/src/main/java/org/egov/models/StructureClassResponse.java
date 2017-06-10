package org.egov.models;

import java.util.List;

public class StructureClassResponse {
	private ResponseInfo responseInfo;

	private List<StructureClass> structureClasses;

	public ResponseInfo getResponseInfo() {
		return responseInfo;
	}

	public void setResponseInfo(ResponseInfo responseInfo) {
		this.responseInfo = responseInfo;
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
		result = prime * result + ((responseInfo == null) ? 0 : responseInfo.hashCode());
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
		StructureClassResponse other = (StructureClassResponse) obj;
		if (responseInfo == null) {
			if (other.responseInfo != null)
				return false;
		} else if (!responseInfo.equals(other.responseInfo))
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
		return "StructureClassResponse [responseInfo=" + responseInfo + ", structureClasses=" + structureClasses
				+ ", getResponseInfo()=" + getResponseInfo() + ", getStructureClasses()=" + getStructureClasses()
				+ ", hashCode()=" + hashCode() + ", getClass()=" + getClass() + ", toString()=" + super.toString()
				+ "]";
	}

	public StructureClassResponse(ResponseInfo responseInfo, List<StructureClass> structureClasses) {
		super();
		this.responseInfo = responseInfo;
		this.structureClasses = structureClasses;
	}

	public StructureClassResponse() {
		super();
		// TODO Auto-generated constructor stub
	}


}
