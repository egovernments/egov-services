package org.egov.tradelicense.notification.web.contract;

import java.math.BigDecimal;

import org.egov.tl.commons.web.contract.AuditDetails;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
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
