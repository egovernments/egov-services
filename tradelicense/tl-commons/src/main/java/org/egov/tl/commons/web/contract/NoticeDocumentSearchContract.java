package org.egov.tl.commons.web.contract;

import javax.validation.constraints.NotNull;

import org.egov.tl.commons.web.contract.enums.ApplicationTypeEnum;
import org.egov.tl.commons.web.contract.enums.DocumentName;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeDocumentSearchContract {

	@JsonProperty("id")
	private Long id;

	private String applicationNumber;

	private String tradeLicenseNumber;

	private ApplicationTypeEnum applicationType;

	@JsonProperty("date")
	private Long applicationDate;

	private String validityYear;

	private String ward;
	
	private String status;
	
	private String statusName;
	
	private String wardName;
	
	private String ownerName;
	
	private String mobileNumber;
	
	private String tradeTitle;

	@JsonProperty("licenseId")
	private Long licenseId;

	@JsonProperty("tenantId")
	private String tenantId;

	@NotNull
	@JsonProperty("documentType")
	private DocumentName documentName;

	@NotNull
	@JsonProperty("fileStoreId")
	private String fileStoreId;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails;

}