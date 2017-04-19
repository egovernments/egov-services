package org.egov.demand.web.contract;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Demand {
	private Long id;
	private BigDecimal taxAmount;
	private BigDecimal collectionAmount;
	private String installment;
	private String moduleName;
	private List<DemandDetails> demandDetails;
	private List<PaymentInfo> paymentInfos;
	private String tenantId;
}
