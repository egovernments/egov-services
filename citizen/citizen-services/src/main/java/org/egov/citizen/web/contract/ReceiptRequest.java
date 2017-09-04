package org.egov.citizen.web.contract;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ReceiptRequest {
	
	@NotNull
	private String tenantId;
	
	private BigDecimal amountPaid;
	
	private String billNumber;
	
	private String returnUrl;
	
	private Long date;
	
	private String biller;
	
	private String transactionId;
	
	private Object otherDetails;
	
	private String billService;
	
	private String consumerCode;
	
	private String serviceRequestId;
	
	
}
