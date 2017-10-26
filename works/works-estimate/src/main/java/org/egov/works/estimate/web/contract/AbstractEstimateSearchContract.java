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
public class AbstractEstimateSearchContract {
	
	@NotNull
	private String tenantId;
	
	private List<String> ids;

	private List<String> adminSanctionNumbers;

	private Integer pageSize;

	private Integer pageNumber;

	private String sortBy;

	private List<String> estimateNumbers;

	private String departmentCode;

	private String fundCode;

	private String functionCode;

	private String budgetHeadCode;
	
	private Long adminSanctionFromDate;

	private Long adminSanctionToDate;

	private Boolean spillOverFlag;

	private String createdBy;

	private List<String> abstractEstimateNumbers;

	private List<String> workIdentificationNumbers;
}
