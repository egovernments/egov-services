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
public class MeasurementBookSearchContract {

	@NotNull
	private String tenantId;
	
	private Integer pageSize;

	private Integer pageNumber;

	private String sortProperty;
	
	private List<String> ids;
	
	private List<String> workOrderNumbers; //autocomplete
	
	private List<String> mbRefNumbers;//autocomplete
	
	private List<String> loaNumbers;//autocomplete
	
	private List<String> detailedEstimateNumbers; //autocomplete

	private List<String> department;

	private Long fromDate;

	private Long toDate;

	private String createdBy;
	
	private List<String> contractorNames;//autocomplete
	
	private List<String> contractorCodes;//autocomplete

	private List<String> workIdentificationNumbers;//autocomplete
	
	private List<String> statuses;
}
