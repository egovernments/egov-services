package org.egov.swm.web.contract;

import java.util.ArrayList;
import java.util.List;

import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.FuelType;
import org.egov.swm.domain.model.InsuranceDetails;
import org.egov.swm.domain.model.ManufacturingDetails;
import org.egov.swm.domain.model.PurchaseInfo;
import org.egov.swm.domain.model.VehicleType;
import org.egov.swm.domain.model.Vendor;

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
public class Employee {

	@JsonProperty("id")
	private Long id;

	@JsonProperty("name")
	private String name;

	@JsonProperty("code")
	private String code;

	@JsonProperty("username")
	private String username;

	@JsonProperty("assignments")
	private final List<Assignment> assignments = new ArrayList<Assignment>(0);

}
