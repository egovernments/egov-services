package org.egov.inv.domain.model;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ReceiptNotesSearchCriteria {
	
	private List<String> mrnNumber;

	private List<String> receiptType;

	private String mrnStatus;

	private String 	supplierCode;

	private String receivingStore;

	private Long receiptDateFrom;

	private Long receiptDateT0;

	private Integer pageSize;

	private Integer offset;

	private Integer pageNumber;

	private String sortBy;

	private String tenantId;

}
