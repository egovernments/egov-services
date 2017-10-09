package org.egov.models;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LatePaymentPenaltyResponse {

	private ResponseInfo responseInfo;

	@NotNull
	private String upicNo;

	@NotNull
	@Size(min = 4, max = 128)
	private String tenantId;

	@NotNull
	private List<Penalty> penalty;
}
