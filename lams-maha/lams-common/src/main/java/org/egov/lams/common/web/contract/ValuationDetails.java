package org.egov.lams.common.web.contract;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ValuationDetails {
	private Long id;
	@NotNull
	private String tenantId;
	@NotNull
	private Long valuationYear;
	@NotNull
	private Integer valuationRate;
	@NotNull
	private Double valuationAmount;
	private String remarks;

}
