package org.egov.works.measurementbook.web.contract;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Contract class to get search request.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ContractorBillSearchContract {

	@NotNull
	private String tenantId;
	
	private Integer pageSize;

	private Integer pageNumber;

	private String sortProperty;
	
	private List<String> ids;
	
	private List<String> letterOfAcceptanceNumbers;
	
	private List<String> billNumbers;
	
	private List<String> departmentCodes;

	private Long billFromDate;

	private Long billToDate;

	private List<String> billTypes;
	
	private List<String> contractorNames;
	
	private List<String> workIdentificationNumbers;
	
	private List<String> statuses;
	
	private Boolean spillOverFlag;
	
	private String letterOfAcceptanceNumberLike;
	
	private String billNumberLike;
	
	private String workIdentificationNumberLike;
	
	private String contractorNameLike;
}
