package org.egov.pt.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Draft {
	
	@JsonProperty("id")
	private String id;
	
	@JsonProperty("userId")
	@NotNull
	private String userId;
	
	@JsonProperty("tenantId")
	@NotNull
	private String tenantId;

    @JsonProperty("isActive")
    private boolean isActive;

    @JsonProperty("assessmentNumber")
    private String assessmentNumber;
	
	@JsonProperty("draftRecord")
	private Object draftRecord;
	
	@JsonProperty("auditDetails")
	private AuditDetails auditDetails;
	
	

}
