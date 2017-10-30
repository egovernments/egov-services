package org.egov.lcms.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaseVoucher {
	
	@JsonProperty("code")
	private String code = null;
	
	@NonNull
	@JsonProperty("caseCode")
	private String caseCode = null;
	
	@JsonProperty("vocherType")
	private VocherType vocherType = null;
	
	@JsonProperty("vocherDate")
	private Long vocherDate = null;
	
	@JsonProperty("details")
	private String details = null;
	
	@JsonProperty("verificationRemarks")
	private String verificationRemarks = null;
	
	@JsonProperty("officerSignature")
	private String officerSignature = null;
	
	

}
