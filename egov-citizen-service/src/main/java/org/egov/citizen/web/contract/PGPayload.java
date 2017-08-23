package org.egov.citizen.web.contract;

import java.math.BigDecimal;

import org.egov.common.contract.request.RequestInfo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class PGPayload {
	
    private String billNumber;
    private String retrunUrl;
    private Long date;
    private String requestHash;
    private String biller;
    private BigDecimal transactionFees;
    private String billServuce;
    private String serviceRequestId;
    private String consumerCode;
    private RequestInfo requestInfo;
    private String tenantId;
    private BigDecimal amountPaid;
    

}
