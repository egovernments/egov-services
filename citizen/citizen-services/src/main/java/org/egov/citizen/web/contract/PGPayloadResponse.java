package org.egov.citizen.web.contract;

import java.math.BigDecimal;

import org.egov.common.contract.request.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class PGPayloadResponse {
	
	private String status;
	private Object errors;
	private String paymentMethod;
    private String billNumber;
    private String responseHash;
	private String transactionId;
	private Object otherDetails;
    private String billService;
    
    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;
    
    private String tenantId;
    private BigDecimal amountPaid;
    private String consumerCode;
    
    @JsonProperty("UID")
    private Long uid;
    private String serviceRequestId;



}
