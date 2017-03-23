package org.egov.lams.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Demand {
	
	private String id;
	private String installment;
	private String moduleName;
	private List<DemandDetails> demandDetails;
	private List<PaymentInfo> paymentInfo;
}
