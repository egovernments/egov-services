package org.egov.lcms.models;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.egov.lcms.enums.NoticeType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Notice {
	
	private String code;
	
	@NotNull
	@Size(min = 4, max = 128)
	private String tenantId ;
	
	private String caseNo;
	
	private String caseCode;
	
	private String caseRefernceNo;
	
	private String summonReferenceNo;
	
	private String exhibitNo;
	
	private String courtName;
	
	private String courtAddress;
	
	private String applicant;
	
	private String defendant;
	
	private String chiefOfficerDetails;
	
	private String advocateName;
	
	private String day;
	
	private List<String> witness;
	
	@NotNull
	@JsonProperty("noticeType")
    private NoticeType noticeType;
	
	@NotNull
    @Size(min = 4, max = 128)
    @JsonProperty("fileStoreId")
    private String fileStoreId;
		
	@JsonProperty("auditDetails")
	private AuditDetails auditDetails;
}
