package org.egov.pgr.rest.web.model;

import java.io.Serializable;
import java.util.Objects;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServiceDefinition implements Serializable  {

	private static final long serialVersionUID = 5942225944569984697L;

	@SerializedName("service_code")
	@Expose
	private String serviceCode = null;

	@SerializedName("attributes")
	@Expose
	private JsonArray attributes;

	public ServiceDefinition(String serviceCode, String attributes){
		this.serviceCode = serviceCode;
		JsonParser  parser = new JsonParser();
		JsonElement element   = parser.parse(attributes);
		JsonArray attributesList = element.getAsJsonArray();
		this.attributes = attributesList;
	}

	public ServiceDefinition serviceCode(String serviceCode) {
		this.serviceCode = serviceCode;
		return this;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public ServiceDefinition attributes(JsonArray attributes) {
		this.attributes = attributes;
		return this;
	}

	public JsonArray getAttributes() {
		return attributes;
	}

	public void setAttributes(JsonArray attributes) {
		this.attributes = attributes;
	}


	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ServiceDefinition serviceDefinition = (ServiceDefinition) o;
		return Objects.equals(this.serviceCode, serviceDefinition.serviceCode) &&
				Objects.equals(this.attributes, serviceDefinition.attributes);
	}

	@Override
	public int hashCode() {
		return Objects.hash(serviceCode, attributes);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ServiceDefinition {\n");

		sb.append("    serviceCode: ").append(toIndentedString(serviceCode)).append("\n");
		sb.append("    attributes: ").append(toIndentedString(attributes)).append("\n");
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

