package org.egov.tl.commons.web.contract;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LicenseFeeDetailContract {

	@JsonProperty("id")
	private Long id;

//	@JsonProperty("licenseId")
//	private Long licenseId;

	@NotEmpty(message = "{error.financialYear.empty}")
	@Length(min = 1, max = 128, message = "{error.financialYear.empty}")
	@Pattern(regexp = ".*[^ ].*", message = "{error.financialYear.emptyspaces}")
	@JsonProperty("financialYear")
	private String financialYear;
	
	@JsonProperty("tenantId")
	private String tenantId;
	
	@JsonProperty("applicationId")
	private Long applicationId;
	
	@NotNull(message = "{error.license.amount}")
	@DecimalMin(value="0", inclusive=false, message="{error.license.min.amount}")
	@Digits(integer = 10, fraction = 2, message = "{error.license.amount.decimal}")
	@JsonProperty("amount")
	private Double amount;

	@JsonProperty("paid")
	private Boolean paid = false;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails;

}