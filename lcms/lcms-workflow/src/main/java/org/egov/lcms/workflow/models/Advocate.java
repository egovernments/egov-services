package org.egov.lcms.workflow.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.egov.models.AuditDetails;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Advocate
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Advocate {

	@NotNull
	@NotEmpty
	@Size(min = 1, max = 100)
	@JsonProperty("name")
	private String name = null;

	@JsonProperty("code")
	private String code = null;

	@NotNull
	@NotEmpty
	@Size(min = 1, max = 100)
	@JsonProperty("organizationName")
	private String organizationName = null;

	@JsonProperty("isIndividual")
	private Boolean isIndividual = true;

	@NotNull
	@NotEmpty
	@JsonProperty("title")
	private String title = null;

	@NotNull
	@NotEmpty
	@JsonProperty("firstName")
	private String firstName = null;

	@NotNull
	@NotEmpty
	@JsonProperty("secondName")
	private String secondName = null;

	@NotNull
	@NotEmpty
	@JsonProperty("lastName")
	private String lastName = null;

	@NotNull
	@NotEmpty
	@JsonProperty("address")
	private String address = null;

	@NotNull
	@NotEmpty
	@JsonProperty("contactNo")
	private String contactNo = null;

	@NotNull
	@JsonProperty("dateOfEmpanelment")
	private Long dateOfEmpanelment = null;

	@NotNull
	@JsonProperty("standingCommitteeDecisionDate")
	private Long standingCommitteeDecisionDate = null;

	@NotNull
	@JsonProperty("empanelmentFromDate")
	private Long empanelmentFromDate = null;

	@NotNull
	@NotEmpty
	@Size(min = 12, max = 12)
	@JsonProperty("aadhar")
	private String aadhar = null;

	@NotNull
	@NotEmpty
	@JsonProperty("gender")
	private String gender = null;

	@NotNull
	@NotEmpty
	@JsonProperty("age")
	private String age = null;

	@NotNull
	@JsonProperty("dob")
	private Long dob = null;

	@NotNull
	@NotEmpty
	@Size(min = 10, max = 10)
	@JsonProperty("mobileNumber")
	private String mobileNumber = null;

	@NotNull
	@NotEmpty
	@Email
	@JsonProperty("emailId")
	private String emailId = null;

	@NotNull
	@NotEmpty
	@Size(min = 12, max = 12)
	@JsonProperty("pan")
	private String pan = null;

	@NotNull
	@NotEmpty
	@JsonProperty("vatTinNo")
	private String vatTinNo = null;

	@NotNull
	@JsonProperty("newsPaperAdvertismentDate")
	private Long newsPaperAdvertismentDate = null;

	@NotNull
	@JsonProperty("empanelmentToDate")
	private Long empanelmentToDate = null;

	@NotNull
	@NotEmpty
	@JsonProperty("bankName")
	private String bankName = null;

	@NotNull
	@NotEmpty
	@JsonProperty("bankBranch")
	private String bankBranch = null;

	@NotNull
	@NotEmpty
	@JsonProperty("bankAccountNo")
	private String bankAccountNo = null;

	@NotNull
	@NotEmpty
	@JsonProperty("isfcCode")
	private String isfcCode = null;

	@NotNull
	@NotEmpty
	@JsonProperty("micr")
	private String micr = null;

	@NotNull
	@JsonProperty("isActive")
	private Boolean isActive = true;

	@NotNull
	@NotEmpty
	@JsonProperty("isTerminate")
	private Boolean isTerminate = false;

	@JsonProperty("inActiveDate")
	private Long inActiveDate = null;

	@JsonProperty("terminationDate")
	private Long terminationDate = null;

	@JsonProperty("reasonOfTermination")
	private String reasonOfTermination = null;

	@NotNull
	@NotEmpty
	@Size(min = 4, max = 128)
	@JsonProperty("tenantId")
	private String tenantId = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;
}