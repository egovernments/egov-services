package org.egov.demand.model;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusinessServiceDetail {

	private String businessService;

	private Double minimumAmountPayable;

	private List<String> collectionModesNotAllowed = new ArrayList<>();

	private Boolean callBackForApportioning;

	private Boolean partPaymentAllowed;
}
