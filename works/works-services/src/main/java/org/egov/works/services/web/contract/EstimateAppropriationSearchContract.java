package org.egov.works.services.web.contract;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Contract class to get search request.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EstimateAppropriationSearchContract {

	@NotNull
	private String tenantId;

	private String objectType;

	private String ObjectNumber;

	private List<String> budgetReferanceNumbers;

	private String sortBy;
	
	private List<String> ids;
	
	private List<String> workOrderNumbers;
	
	private List<String> detailedEstimateNumbers;

	private List<String> abstractEstimateNumbers;
}
