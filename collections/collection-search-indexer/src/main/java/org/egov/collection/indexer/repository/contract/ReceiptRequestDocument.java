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

    @JsonProperty("totalAmount")
    private BigDecimal totalAmount;

    @JsonProperty("channel")
    private String channel;

    @JsonProperty("paymentGateway")
    private String paymentGateway;

    @JsonProperty("billNumber")
    private String billNumber;

    @JsonProperty("consumerCode")
    private String consumerCode;

    @JsonProperty("status")
    private String status;

    @JsonProperty("consumerName")
    private String consumerName;

    @JsonProperty("receiptCreator")
    private String receiptCreator;

    @JsonProperty("consumerType")
    private String consumerType;

    @JsonProperty("cityName")
    private String cityName;

    @JsonProperty("districtName")
    private String districtName;

    @JsonProperty("regionName")
    private String regionName;
}
