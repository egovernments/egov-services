package org.egov.works.estimate.web.contract;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.*;

/**
 * Contract class to get search request.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AbstractEstimateDetailsSearchContract {

	@NotNull
	private String tenantId;

	private List<String> ids;

	private List<String> abstractEstimateIds;

	private Integer pageSize;

	private Integer pageNumber;

	private String sortBy;

	private List<String> workIdentificationNumbers;
}
