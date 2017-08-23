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
	
	@JsonProperty("tenantId")
	@NotNull
	private String tenantId;
	
	@JsonProperty("amountPaid")
	@NotNull
	private BigDecimal amountPaid;
	
	@JsonProperty("status")
	@NotNull
	private List<String> status; //This should be an enum
	
	@JsonProperty("billNumber")
	private String billNumber;
	
	@JsonProperty("responseHash")
	@NotNull
	private String responseHash;
	
	@JsonProperty("transactionId")
	private String transactionId;
	
	@JsonProperty("otherDetails")
	private Object otherDetails;
	
	@JsonProperty("billService")
	@NotNull
	private Object billService;
	
	@JsonProperty("consumerCode")
	private String consumerCode;
	
	
}
