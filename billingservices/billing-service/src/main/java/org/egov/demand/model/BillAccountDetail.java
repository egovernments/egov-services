package org.egov.demand.model;

import org.egov.demand.model.enums.Purpose;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillAccountDetail {

	private String id;

	private String tenantId;

	private String billDetail;
	
	private String glcode;

	private Integer order;

	private String accountDescription;

	private Double creditAmount;

	private Double debitAmount;

	private Boolean isActualDemand;

	private Purpose purpose ;
}