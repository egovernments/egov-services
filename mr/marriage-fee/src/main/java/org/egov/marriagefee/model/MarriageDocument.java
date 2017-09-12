package org.egov.marriagefee.model;

import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@Builder
public class MarriageDocument {
	
	private String id ;
	
	@NotNull
	private String documentType = null;

	@NotNull
	private String location = null;
	
	@NotNull
	private String tenantId = null;
	
	@NotNull
	private String reissueCertificateId ;
	
	private AuditDetails  auditDetails;
}
