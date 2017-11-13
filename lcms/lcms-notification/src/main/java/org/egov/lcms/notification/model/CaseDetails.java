package org.egov.lcms.notification.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CaseDetails {

	@JsonProperty("caseNo")
	private String caseNo;
	
	@JsonProperty("summonReferenceNo")
	private String summonReferenceNo;
}

