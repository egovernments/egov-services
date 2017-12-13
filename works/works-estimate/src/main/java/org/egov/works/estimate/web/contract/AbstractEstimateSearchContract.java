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
public class AbstractEstimateSearchContract {
	
	@NotNull
	private String tenantId;
	
	private List<String> ids;

	private List<String> adminSanctionNumbers;

	private Integer pageSize;

	private Integer pageNumber;

	private String sortBy;

	private List<String> departments;

	private List<String> fundCodes;

	private List<String> functionCodes;

	private List<String> budgetHeadCodes;
	
	private Long adminSanctionFromDate;

	private Long adminSanctionToDate;

	private Boolean spillOverFlag;

	private String createdBy;

	private List<String> abstractEstimateNumbers;

	private List<String> workIdentificationNumbers;
	
	private List<String> statuses;
	
	private String nameOfWork;
	
	private List<String> councilSanctionNumbers;
}
