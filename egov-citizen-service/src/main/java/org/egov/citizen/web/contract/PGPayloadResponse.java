package org.egov.citizen.web.contract;

import java.math.BigDecimal;

import org.egov.common.contract.request.RequestInfo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class PGPayloadResponse {
	
    private String billNumber;
    private String responseHash;
    private String billService;
    private String serviceRequestId;
    private String consumerCode;
    private RequestInfo requestInfo;
    private String tenantId;
    private BigDecimal amountPaid;
	private String transactionId;


}
