package org.egov.pa.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ValueDocument {
	
	@JsonProperty("id")
	private String id;
	
	@JsonProperty("code")
	private String documentCode;
	
	@JsonProperty("kpiCode")
	private String kpiCode;
	
	@JsonProperty("valueId")
	private String valueId;
	
	@JsonProperty("fileStoreId")
	private String fileStoreId;
	
	@JsonProperty("description")
	private String description;
	
	@JsonProperty("auditDetails")
	private AuditDetails auditDetails;
	
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public AuditDetails getAuditDetails() {
		return auditDetails;
	}
	public void setAuditDetails(AuditDetails auditDetails) {
		this.auditDetails = auditDetails;
	}
	public String getDocumentCode() {
		return documentCode;
	}
	public void setDocumentCode(String documentCode) {
		this.documentCode = documentCode;
	}
	public String getKpiCode() {
		return kpiCode;
	}
	public void setKpiCode(String kpiCode) {
		this.kpiCode = kpiCode;
	}
	public String getValueId() {
		return valueId;
	}
	public void setValueId(String valueId) {
		this.valueId = valueId;
	}
	public String getFileStoreId() {
		return fileStoreId;
	}
	public void setFileStoreId(String fileStoreId) {
		this.fileStoreId = fileStoreId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	} 
	
	
	

}
