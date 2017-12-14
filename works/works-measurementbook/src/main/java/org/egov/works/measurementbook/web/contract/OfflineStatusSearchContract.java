package org.egov.works.measurementbook.web.contract;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OfflineStatusSearchContract {

	private String tenantId;

	private List<String> loaNumbers;

	private List<String> workOrderNumbers;
	
	private List<String> detailedEstimateNumbers;

	private String objectType;
	
	private List<String> statuses;

	private List<String> ids;

	private Integer pageSize;

	private Integer pageNumber;

	private String sortBy;

}
