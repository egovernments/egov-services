package org.egov.tlcalculator.web.models.demand;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.tlcalculator.web.models.enums.Status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillDetail {

	private String id;
	//TODO do we need to tenantId in child when parent is having?
	private String tenantId;
	
	private String bill;

	private String businessService;

	private String billNumber;

	private Long billDate;

	private String consumerCode;

	private String consumerType;

	private String billDescription;

	private String displayMessage;
	
	private Long receiptDate;
	
	private String receiptNumber;

	private BigDecimal minimumAmount;

	private BigDecimal totalAmount;
	
	private BigDecimal collectedAmount;

	private List<String> collectionModesNotAllowed = new ArrayList<>();

	private Boolean callBackForApportioning;

	private Boolean partPaymentAllowed;
	
	private Status status;

	private List<BillAccountDetail> billAccountDetails = new ArrayList<>();
}