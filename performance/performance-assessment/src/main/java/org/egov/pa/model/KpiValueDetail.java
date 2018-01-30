package org.egov.pa.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KpiValueDetail {
	
	@JsonProperty("id")
	private String id = null; 
	
	@JsonProperty("valueid")
	private String valueid = null;
	
	@JsonProperty("period")
	private String period = null; 
	
	@JsonProperty("value")
	private String value = null;
	
	@JsonProperty("documents")
	private List<ValueDocument> documentList = null; 
	
	@JsonProperty("kpiCode")
	private String kpiCode = null; 
	
	@JsonProperty("remarks")
	private String remarks = null; 
	
	@JsonProperty("createdBy")
	private Long createdBy = null;
	
	@JsonProperty("createdTime")
	private Long createdTime = null; 
	
	
	
	
	
	
	
	

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Long getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Long createdTime) {
		this.createdTime = createdTime;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getKpiCode() {
		return kpiCode;
	}

	public void setKpiCode(String kpiCode) {
		this.kpiCode = kpiCode;
	}

	public List<ValueDocument> getDocumentList() {
		return documentList;
	}

	public void setDocumentList(List<ValueDocument> documentList) {
		this.documentList = documentList;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValueid() {
		return valueid;
	}

	public void setValueid(String valueid) {
		this.valueid = valueid;
	}

	
	
	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	} 

	
	
}
