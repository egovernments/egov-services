package org.egov.lcms.notification.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

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

	@Size(min = 1, max = 100)
	@JsonProperty("name")
	private String name ;

	@JsonProperty("code")
	private String code ;

	@Size(min = 1, max = 100)
	@JsonProperty("organizationName")
	private String organizationName ;

	@JsonProperty("isIndividual")
	private Boolean isIndividual = true;

	@NotNull	
	@JsonProperty("title")
	private String title ;

	@NotNull	
	@JsonProperty("firstName")
	private String firstName ;

	@NotNull	
	@JsonProperty("secondName")
	private String secondName ;

	@NotNull	
	@JsonProperty("lastName")
	private String lastName ;

	@NotNull	
	@JsonProperty("address")
	private String address ;

	@NotNull	
	@JsonProperty("contactNo")
	private String contactNo ;

	@NotNull
	@JsonProperty("dateOfEmpanelment")
	private Long dateOfEmpanelment ;

	@NotNull
	@JsonProperty("standingCommitteeDecisionDate")
	private Long standingCommitteeDecisionDate ;

	@NotNull
	@JsonProperty("empanelmentFromDate")
	private Long empanelmentFromDate ;

	@NotNull	
	@Size(min = 12, max = 12)
	@JsonProperty("aadhar")
	private String aadhar ;

	@NotNull	
	@JsonProperty("gender")
	private String gender ;

	@NotNull	
	@JsonProperty("age")
	private String age ;

	@NotNull
	@JsonProperty("dob")
	private Long dob ;

	@NotNull	
	@Size(min = 10, max = 10)
	@JsonProperty("mobileNumber")
	private String mobileNumber ;

	@NotNull	
	@Email
	@JsonProperty("emailId")
	private String emailId ;

	@NotNull	
	@Size(min = 10, max = 10)
	@JsonProperty("pan")
	private String pan ;

	@JsonProperty("vatTinNo")
	private String vatTinNo ;

	@NotNull
	@JsonProperty("newsPaperAdvertismentDate")
	private Long newsPaperAdvertismentDate ;

	@NotNull
	@JsonProperty("empanelmentToDate")
	private Long empanelmentToDate ;

	@NotNull	
	@JsonProperty("bankName")
	private String bankName ;

	@NotNull	
	@JsonProperty("bankBranch")
	private String bankBranch ;

	@NotNull	
	@JsonProperty("bankAccountNo")
	private String bankAccountNo ;

	@NotNull	
	@JsonProperty("isfcCode")
	private String isfcCode ;

	@NotNull
	@JsonProperty("micr")
	private String micr ;

	@JsonProperty("isActive")
	private Boolean isActive = true;
	
	@JsonProperty("isTerminate")
	private Boolean isTerminate = false;

	@JsonProperty("inActiveDate")
	private Long inActiveDate ;

	@JsonProperty("terminationDate")
	private Long terminationDate ;

	@JsonProperty("reasonOfTermination")
	private String reasonOfTermination ;

	@NotNull
	@Size(min = 4, max = 128)
	@JsonProperty("tenantId")
	private String tenantId ;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails ;
}