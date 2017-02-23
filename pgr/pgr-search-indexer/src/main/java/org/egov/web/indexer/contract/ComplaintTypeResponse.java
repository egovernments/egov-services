package org.egov.web.indexer.contract;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class ComplaintTypeResponse {

	@JsonProperty("ComplaintType")
	private List<ComplaintType> complaintTypes;

}