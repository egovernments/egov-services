
package org.egov.eis.index.model;


import java.util.Date;


import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
public class EmployeeDetails {

    @JsonProperty("ulbname")
	private String ulbName;

	@JsonProperty("ulbcode")
	private String ulbCode;

	@JsonProperty("distname")
	private String distName;

	@JsonProperty("regname")
	private String regName;

	@JsonProperty("ulbgrade")
	private String ulbGrade;

	@JsonProperty("employeeid")
	private long employeeId;

	@JsonProperty("employeecode")
	private String employeeCode;

	@JsonProperty("dateofappointment")
	private Date dateOfAppointment;

	@JsonProperty("dateofretirement")
	private Date dateOfRetirement;

	@JsonProperty("employeestatus")
	private String employeeStatus;

	@JsonProperty("employeetype")
	private String employeeType;

	@JsonProperty("username")
	private String userName;

	@JsonProperty("pwdexpirydate")
	private Date pwdExpiryDate;

	@JsonProperty("mobilenumber")
	private String mobileNumber;

	@JsonProperty("alternatenumber")
	private String alternateNumber;

	@JsonProperty("emailid")
	private String emailId;

	@JsonProperty("employeeactive")
	private Boolean employeeActive;

	@JsonProperty("salutation")
	private String salutation;

	@JsonProperty("employeename")
	private String employeeName;

	@JsonProperty("usertype")
	private String userType;

	@JsonProperty("gender")
	private String gender;

	@JsonProperty("pannumber")
	private String panNumber;

	@JsonProperty("aadharnumber")
	private String aadharNumber;

	@JsonProperty("dateofbirth")
	private Date dateOfBirth;

	@JsonProperty("employeecreateddate")
	private Date employeeCreatedDate;

	@JsonProperty("employeecreatedby")
	private String employeeCreatedBy;

	@JsonProperty("recruitmentmode")
	private String recruitmentMode;

	@JsonProperty("recruitmenttype")
	private String recruitmentType;

	@JsonProperty("recruitmentquota")
	private String recruitmentQuota;

	@JsonProperty("retirementage")
	private int retirementAge;

	@JsonProperty("dateofresignation")
	private Date dateOfResignation;

	@JsonProperty("dateoftermination")
	private Date dateOfTermination;

	@JsonProperty("mothertongue")
	private String motherTongue;

	@JsonProperty("religion")
	private String religion;

	@JsonProperty("community")
	private String community;

	@JsonProperty("employeecategory")
	private String employeeCategory;

	@JsonProperty("physicallyDisabled")
	private Boolean physicallyDisabled;

	@JsonProperty("medicalReportProduced")
	private Boolean medicalReportProduced;

	@JsonProperty("languagesknown")
	private String languagesKnown;

	@JsonProperty("maritalstatus")
	private String maritalStatus;

	@JsonProperty("passportno")
	private String passportNo;

	@JsonProperty("gpfno")
	private String gpfNo;

	@JsonProperty("bank")
	private String bank;

	@JsonProperty("bankbranch")
	private String bankBranch;

	@JsonProperty("bankaccount")
	private String bankAccount;

	@JsonProperty("employeegroup")
	private String employeeGroup;

	@JsonProperty("placeofbirth")
	private String placeOfBirth;

	@JsonProperty("permanentaddress")
	private String permanentAddress;

	@JsonProperty("permanentcity")
	private String permanentCity;

	@JsonProperty("permanentpincode")
	private String permanentPinCode;

	@JsonProperty("correspondencecity")
	private String correspondenceCity;

	@JsonProperty("correspondencepincode")
	private String correspondencePinCode;

	@JsonProperty("correspondenceaddress")
	private String correspondenceAddress;

	@JsonProperty("accountlocked")
	private Boolean accountLocked;

	@JsonProperty("fatherorhusbandname")
	private String fatherOrHusbandname;

	@JsonProperty("bloodgroup")
	private String bloodGroup;

	@JsonProperty("identificationmark")
	private String identificationMark;


}