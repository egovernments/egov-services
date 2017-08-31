package org.egov.citizen.web.contract;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.egov.common.contract.request.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ReceiptRequest {

	@JsonProperty("RequestInfo")
	@NotNull
	private RequestInfo requestInfo;
	
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
