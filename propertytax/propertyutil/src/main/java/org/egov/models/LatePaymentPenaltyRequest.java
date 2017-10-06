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
public class LatePaymentPenaltyRequest {

	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;

	@NotNull
	private String upicNo;

	@NotNull
	@Size(min = 4, max = 128)
	private String tenantId;
}