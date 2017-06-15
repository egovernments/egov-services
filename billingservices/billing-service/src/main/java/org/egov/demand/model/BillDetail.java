package org.egov.demand.model;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillDetail {

	private Long id;

	private String businessService;

	private String billNumber;

	private Long billDate;

	private String consumerCode;

	private String consumerType;

	private String billDescription;

	private String displayMessage;

	private Double minimumAmount;

	private Double totalAmount;

	private List<String> collectionModesNotAllowed = new ArrayList<>();

	private Boolean callBackForApportioning;

	private Boolean partPaymentAllowed;

	private List<BillAccountDetail> billAccountDetails = new ArrayList<>();
}