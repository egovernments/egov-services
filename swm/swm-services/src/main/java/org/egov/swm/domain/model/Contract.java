package org.egov.swm.domain.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.egov.swm.domain.model.Contractor.ContractorBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

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
public class Contract {

	@NotNull
	@Size(min = 1, max = 128)
	@JsonProperty("tenantId")
	private String tenantId = null;

	@Size(min = 6, max = 128)
	@JsonProperty("contractNo")
	private String contractNo = null;

	@NotNull
	@JsonProperty("contractDate")
	private Long contractDate = null;

	@NotNull
	@JsonProperty("contractPeriodFrom")
	private Long contractPeriodFrom = null;

	@NotNull
	@JsonProperty("contractPeriodTo")
	private Long contractPeriodTo = null;

	@NotNull
	@JsonProperty("securityDeposit")
	private Double securityDeposit = null;

	@NotNull
	@JsonProperty("servicesOffered")
	private List<Vendor> servicesOffered = new ArrayList<Vendor>();

	@NotNull
	@JsonProperty("paymentTerms")
	private PaymentTerms paymentTerms = null;

	@Size(min = 15, max = 500)
	@JsonProperty("remarks")
	private String remarks = null;

}
