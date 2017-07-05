package org.egov.collection.indexer.repository.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceiptRequestDocument {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("tenantId")
    private String tenantId;

    @JsonProperty("receiptDate")
    private String receiptDate;

    @JsonProperty("receiptNumber")
    private String receiptNumber;

    @JsonProperty("billingService")
    private String billingService;

    @JsonProperty("paymentMode")
    private String paymentMode;

    @JsonProperty("arrearAmount")
    private BigDecimal arrearAmount;

    @JsonProperty("penaltyAmount")
    private BigDecimal penaltyAmount;

    @JsonProperty("currentAmount")
    private BigDecimal currentAmount;

    @JsonProperty("totalAmount")
    private BigDecimal totalAmount;

    @JsonProperty("advanceAmount")
    private BigDecimal advanceAmount;

    @JsonProperty("channel")
    private String channel;

    @JsonProperty("paymentGateway")
    private String paymentGateway;

    @JsonProperty("billNumber")
    private String billNumber;

    @JsonProperty("consumerCode")
    private String consumerCode;

    @JsonProperty("cityName")
    private String cityName;

    @JsonProperty("districtName")
    private String districtName;

    @JsonProperty("regionName")
    private String regionName;

    @JsonProperty("status")
    private String status;

    @JsonProperty("latePaymentCharges")
    private BigDecimal latePaymentCharges;

    @JsonProperty("arrearCess")
    private BigDecimal arrearCess;

    @JsonProperty("currentCess")
    private BigDecimal currentCess;

    @JsonProperty("installmentFrom")
    private String installmentFrom;

    @JsonProperty("installmentTo")
    private String installmentTo;

    @JsonProperty("consumerName")
    private String consumerName;

    @JsonProperty("reductionAmount")
    private BigDecimal reductionAmount;

    @JsonProperty("cityGrade")
    private String cityGrade;

    @JsonProperty("cityCode")
    private String cityCode;

    @JsonProperty("receiptCreator")
    private String receiptCreator;

    @JsonProperty("revenueWard")
    private String revenueWard;

    @JsonProperty("consumerType")
    private String consumerType;
}
