package org.egov.lcms.notification.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CaseVoucher {
	
	@JsonProperty("code")
	private String code = null;
	
	@JsonProperty("caseCode")
	private String caseCode = null;
	
	@JsonProperty("voucherType")
	private VocherType voucherType;
	
	@JsonProperty("voucherDate")
	private Long voucherDate = null;
	
	@JsonProperty("details")
	private String details = null;
	
	@JsonProperty("verificationRemarks")
	private String verificationRemarks = null;
	
	@JsonProperty("officerSignature")
	private String officerSignature = null;
	
	@JsonProperty("tenantId")
	private String tenantId = null;
	
	

}
