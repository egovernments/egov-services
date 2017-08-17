package org.egov.tl.commons.web.contract;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

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

	@JsonProperty("licenseId")
	private Long licenseId;

	@NotEmpty(message="financialYear is required, Please enter valid financialYear")
	@Length(min =1, max = 128,message="financialYear is required, Please enter valid financialYear")
	@JsonProperty("financialYear")
	private String financialYear;

	@NotNull(message="{error.license.amount}")
	@Min(1)
	@Digits(integer=10, fraction=2,message="{error.license.amount.decimal}")
	@JsonProperty("amount")
	private Double amount;

	@JsonProperty("paid")
	private Boolean paid = false;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails;

}