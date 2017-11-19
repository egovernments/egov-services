package org.egov.works.workorder.web.contract;

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
public class DetailedEstimateSearchContract {
	
	@NotNull
	private String tenantId;
	
	private List<String> ids;

	private List<String> detailedEstimateNumbers;
	
	private Long fromDate;

	private Long toDate;
	
	private Long fromAmount;

	private Long toAmount;
	
	private String department;
	
	private List<String> typeOfWork;
	
	private List<String> subTypeOfWork;
	
	private List<String> statuses;

	private List<String> workIdentificationNumbers;

	private List<String> abstractEstimateNumbers;
	
	private Integer pageSize;

	private Integer pageNumber;

	private String sortBy;

	private String nameOfWork;
	
	private String estimateAmount;
	
	private List<String> wards;

	private String currentOwner;

	private Boolean spillOverFlag;

	private String createdBy;

    private List<String> technicalSanctionNumbers;
}
