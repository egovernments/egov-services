package org.egov.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Contract class to receive request. Array of Property items  are used in case of create . Where as single Property item is used for update
 */

public class PropertyRequest   {
	@JsonProperty("requestInfo")
	private RequestInfo requestInfo = null;

	@JsonProperty("properties")
	private List<Property> properties = new ArrayList<Property>();

	/**
	 * Get requestInfo
	 * @return requestInfo
	 **/
	public RequestInfo getRequestInfo() {
		return requestInfo;
	}

	public void setRequestInfo(RequestInfo requestInfo) {
		this.requestInfo = requestInfo;
	}

	/**
	 * Used for search result and create only
	 * @return properties
	 **/
	public List<Property> getProperties() {
		return properties;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}


	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		PropertyRequest propertyRequest = (PropertyRequest) o;
		return Objects.equals(this.requestInfo, propertyRequest.requestInfo) &&
				Objects.equals(this.properties, propertyRequest.properties);
	}

	@Override
	public int hashCode() {
		return Objects.hash(requestInfo, properties);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class PropertyRequest {\n");

		sb.append("    requestInfo: ").append(toIndentedString(requestInfo)).append("\n");
		sb.append("    properties: ").append(toIndentedString(properties)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(java.lang.Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}

