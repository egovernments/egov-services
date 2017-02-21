package org.egov.web.indexer.contract;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ComplaintType {

	private Integer slaHours;

	public Integer getSlaHours() {
		return slaHours;
	}

	public void setSlaHours(Integer slaHours) {
		this.slaHours = slaHours;
	}


}