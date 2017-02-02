package org.egov.workflow.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AssigneeResponseInfo {
	
	@JsonProperty("code")
	private String code;
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("responseInfo")
	private ResponseInfo responseInfo = null;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ResponseInfo getResponseInfo() {
		return responseInfo;
	}

	public void setResponseInfo(ResponseInfo responseInfo) {
		this.responseInfo = responseInfo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(responseInfo, code ,name); 
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class AssigneeResponse {\n");
		sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
		sb.append("    name: ").append(toIndentedString(name)).append("\n");
		sb.append("    code: ").append(toIndentedString(code)).append("\n");
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
