package org.egov.eis.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.egov.eis.entity.PositionHierarchy;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PositionHierarchyRes {
	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo = null;

	@JsonProperty("PositionHierarchies")
	private List<PositionHierarchy> positionHierarchies = new ArrayList<PositionHierarchy>();

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

	public PositionHierarchyRes positionHierarchy(List<PositionHierarchy> positionHierarchies) {
		this.positionHierarchies = positionHierarchies;
		return this;
	}

	public PositionHierarchyRes addPositionHierarchyItem(PositionHierarchy positionHierarchyItem) {
		this.positionHierarchies.add(positionHierarchyItem);
		return this;
	}

	public List<PositionHierarchy> getPositionHierarchies() {
		return positionHierarchies;
	}

	public void setPositionHierarchies(List<PositionHierarchy> positionHierarchies) {
		this.positionHierarchies = positionHierarchies;
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
				&& Objects.equals(this.positionHierarchies, positionHierarchyRes.positionHierarchies);
	}

	@Override
	public int hashCode() {
		return Objects.hash(responseInfo, positionHierarchies);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class PositionHierarchyRes {\n");

		sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
		sb.append("    position: ").append(toIndentedString(positionHierarchies)).append("\n");
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
