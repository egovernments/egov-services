package org.egov.pgr.rest.web.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Service implements Serializable {

	private static final long serialVersionUID = 4218331701543171474L;

	@JsonProperty("service_code")
	private String code;
	
	@JsonProperty("service_name")
	private String name;
	
	@JsonProperty("description")
	private String description;
	
	@JsonProperty("metadata")
	private Boolean metadata;
	
	@JsonProperty("type")
	private String type;
	
	@JsonProperty("keywords")
	private String keywords;
	
	@JsonProperty("group")
	private String category;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getMetadata() {
		return metadata;
	}

	public void setMetadata(Boolean metadata) {
		this.metadata = metadata;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
}