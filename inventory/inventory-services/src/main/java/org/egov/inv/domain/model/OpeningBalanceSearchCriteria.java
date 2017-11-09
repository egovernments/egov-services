package org.egov.inv.domain.model;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class OpeningBalanceSearchCriteria {
	private List<String> id;

	private List<String> mrnNumber;

	private String receiptNumber;
	
	private String materialName;
	
	private String 	supplierCode;
	
	private String finanncilaYear;
	
	private Integer pageSize;

	private Integer offset;

	private Integer pageNumber;

	private String sortBy;

	private String tenantId;




}
