package org.egov.swm.domain.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
public class VendorContract {

	@Size(min = 1, max = 256)
	@JsonProperty("id")
	private String id = null;

	@NotNull
	@Size(min = 1, max = 128)
	@JsonProperty("tenantId")
	private String tenantId = null;

	@NotNull
	@Valid
	@JsonProperty("vendor")
	private Vendor vendor = null;

	@NotNull
	@Valid
	@Size(min = 1)
	@JsonProperty("contracts")
	private List<Contract> contracts = new ArrayList<Contract>();

	@Valid
	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

}
