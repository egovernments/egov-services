package org.egov.works.estimate.web.contract;

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
public class EstimateActivitySearchContract {

	@NotNull
	private String tenantId;

	private List<String> ids;

	private List<String> detailedEstimateIds;

	private Integer pageSize;

	private Integer pageNumber;

	private String sortBy;

}
