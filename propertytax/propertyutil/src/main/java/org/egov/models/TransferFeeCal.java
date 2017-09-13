package org.egov.models;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * 
 * @author Yosadhara
 *
 */
public class TransferFeeCal {

	@JsonProperty("feeFactor")
	@NotNull
	private String feeFactor;

	@JsonProperty("validValue")
	private Double validValue;

	@JsonProperty("validDate")
	@NotNull
	private String validDate;

	@JsonProperty("transferFee")
	private Double transferFee;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails;
}
