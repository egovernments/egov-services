package org.egov.pa.model;

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
