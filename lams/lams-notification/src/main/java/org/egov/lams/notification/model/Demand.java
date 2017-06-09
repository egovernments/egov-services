package org.egov.lams.notification.model;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
@ToString
@EqualsAndHashCode
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
