package org.egov.collection.model;

import groovy.transform.builder.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OnlinePayment {

    private Long id;

    private String receiptHeader;

    private String paymentGatewayName;

    private Long transactionDate;

    private BigDecimal transactionAmount;

    private String transactionNumber;

    private String authorisationStatusCode;

    private String status;

    private String remarks;

    private String callBackUrl;

    private String tenantId;

    private AuditDetails auditDetails;


}
