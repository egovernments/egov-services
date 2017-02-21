package org.egov.eis.web.contract;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CategoryReq {
	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo = null;

	@JsonProperty("Category")
	private Category category = null;

	public CategoryReq requestInfo(RequestInfo requestInfo) {
		this.requestInfo = requestInfo;
		return this;
	}

	public RequestInfo getRequestInfo() {
		return requestInfo;
	}

	public void setRequestInfo(RequestInfo requestInfo) {
		this.requestInfo = requestInfo;
	}

	public CategoryReq category(Category category) {
		this.category = category;
		return this;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		CategoryReq categoryReq = (CategoryReq) o;
		return Objects.equals(this.requestInfo, categoryReq.requestInfo)
				&& Objects.equals(this.category, categoryReq.category);
	}

	@Override
	public int hashCode() {
		return Objects.hash(requestInfo, category);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class CategoryReq {\n");

		sb.append("    requestInfo: ").append(toIndentedString(requestInfo)).append("\n");
		sb.append("    category: ").append(toIndentedString(category)).append("\n");
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
