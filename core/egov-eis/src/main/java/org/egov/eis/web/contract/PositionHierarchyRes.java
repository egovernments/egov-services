package org.egov.eis.web.contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.egov.eis.persistence.entity.PositionHierarchy;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PositionHierarchyRes {
	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo = null;

	@JsonProperty("positionHierarchy")
	private List<PositionHierarchy> positionHierarchy = new ArrayList<PositionHierarchy>();

	public PositionHierarchyRes responseInfo(ResponseInfo responseInfo) {
		this.responseInfo = responseInfo;
		return this;
	}

	public ResponseInfo getResponseInfo() {
		return responseInfo;
	}

	public void setResponseInfo(ResponseInfo responseInfo) {
		this.responseInfo = responseInfo;
	}


	public List<PositionHierarchy> getPositionHierarchy() {
		return positionHierarchy;
	}

	public void setPositionHierarchy(List<PositionHierarchy> positionHierarchy) {
		this.positionHierarchy = positionHierarchy;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		PositionHierarchyRes positionHierarchyRes = (PositionHierarchyRes) o;
		return Objects.equals(this.responseInfo, positionHierarchyRes.responseInfo)
				&& Objects.equals(this.positionHierarchy, positionHierarchyRes.positionHierarchy);
	}

	@Override
	public int hashCode() {
		return Objects.hash(responseInfo, positionHierarchy);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class PositionHierarchyRes {\n");

		sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
		sb.append("    position: ").append(toIndentedString(positionHierarchy)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	private String toIndentedString(java.lang.Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}
