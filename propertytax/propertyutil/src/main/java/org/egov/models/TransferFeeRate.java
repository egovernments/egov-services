package org.egov.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.egov.enums.TransferFeeRatesEnum;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
/**
 * 
 * @author Yosadhara
 *
 */
public class TransferFeeRate {

	private Long id;

	@JsonProperty("tenantId")
	@NotNull
	@Size(min = 4, max = 128)
	private String tenantId;

	@JsonProperty("feeFactor")
	@NotNull
	private TransferFeeRatesEnum feeFactor;

	@JsonProperty("fromDate")
	@NotNull
	private String fromDate;

	@JsonProperty("toDate")
	private String toDate;

	@JsonProperty("fromValue")
	@NotNull
	private Double fromValue;

	@JsonProperty("toValue")
	@NotNull
	private Double toValue;

	@JsonProperty("feePercentage")
	private Double feePercentage;

	@JsonProperty("flatValue")
	private Double flatValue;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails;
}
