package org.egov.lams.model;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Demand {
	private String id;
	private BigDecimal taxAmount;
	private BigDecimal collectionAmount;
	private String installment;
	private String moduleName;
	private List<DemandDetails> demandDetails;
	private List<PaymentInfo> paymentInfos;
	private Double minAmountPayable=0d;
	private String tenantId;
}
