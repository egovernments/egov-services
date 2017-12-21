package org.egov.lcms.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * @author Prasad
 *	This object holds information about the case details
 */
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

