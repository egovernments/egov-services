package org.egov.citizen.web.contract;

import java.math.BigDecimal;

import org.egov.common.contract.request.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PGPayload {
	
    private String billNumber;
    private String retrunUrl;
    private Long date;
    private String requestHash;
    private String biller;
    private BigDecimal transactionFees;
    private String billService;
    private String serviceRequestId;
    private String consumerCode;
    
    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;
    
    private String tenantId;
    private BigDecimal amountPaid;
    private String mobileNo;
    private String email;
    private Long uid;
    

}
