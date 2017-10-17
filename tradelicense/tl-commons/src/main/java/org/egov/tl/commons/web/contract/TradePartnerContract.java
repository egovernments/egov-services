package org.egov.tl.commons.web.contract;

import javax.validation.constraints.Pattern;

import org.egov.tl.commons.web.contract.enums.Gender;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TradePartnerContract {

	private Long id;

	@JsonProperty("tenantId")
	@Pattern(regexp = ".*[^ ].*", message = "{error.license.tenantId.emptyspaces}")
	@NotEmpty(message = "{error.license.tenantId.empty}")
	@Length(min = 4, max = 128, message = "{error.license.tenantId.empty}")
	private String tenantId;

	@JsonProperty("licenseId")
	private Long licenseId;

	@JsonProperty("aadhaarNumber")
	@Pattern(regexp = "[0-9]{12}", message = "{error.license.partneraadhaarNumber}")
	@Length(min = 12, max = 12, message = "{error.license.partneraadhaarNumber}")
	private String aadhaarNumber;

	@JsonProperty("fullName")
	private String fullName;

	@JsonProperty("gender")
	private Gender gender;

	@JsonProperty("birthYear")
	private String birthYear;

	@Length(min = 6, max = 50, message = "{error.license.partneremailid}")
	@Email(message = "{error.license.partneremailId}")
	@NotEmpty(message = "{error.license.partneremailId}")
	@JsonProperty("emailId")
	private String emailId;

	@JsonProperty("designation")
	private String designation;

	@JsonProperty("residentialAddress")
	private String residentialAddress;

	@Length(min = 3, max = 250, message = "{error.license.partnercorrespondenceAddress}")
	@JsonProperty("correspondenceAddress")
	private String correspondenceAddress;

	@Length(min = 10, max = 10, message = "{error.license.partnerphonenumber}")
	@JsonProperty("phoneNumber")
	private String phoneNumber;

	@NotEmpty(message = "{error.license.partnermobilenumber}")
	@Length(min = 10, max = 10, message = "{error.license.partnermobilenumber}")
	@JsonProperty("mobileNumber")
	private String mobileNumber;

	@JsonProperty("photo")
	private String photo;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails;
}
