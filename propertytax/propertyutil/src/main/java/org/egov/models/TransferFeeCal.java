package org.egov.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
public class TransferFeeCal {

	@JsonProperty("tenantId")
	@NotNull
	@Size(min = 4, max = 128)
	private String tenantId;

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
}
