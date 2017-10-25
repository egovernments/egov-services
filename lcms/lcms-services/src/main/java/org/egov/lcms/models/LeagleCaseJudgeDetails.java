package org.egov.lcms.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This object holds information about the legale case hearing judge details
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeagleCaseJudgeDetails {
	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("judgeName")
	@NotNull
	@NotEmpty
	private String judgeName = null;

	@JsonProperty("mobileNumber")
	@Pattern(regexp="[0-9]")
	@Size(min=10,max=10)
	private String mobileNumber = null;

	@JsonProperty("gender")
	private String gender = null;

	@JsonProperty("address")
	private String address = null;

	@JsonProperty("tenantId")
	@NotNull
	@NotEmpty
	private String tenantId = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;
	
	@JsonProperty("referenceNo")
	@NotNull
	@NotEmpty
	private String referenceNo = null;

}
