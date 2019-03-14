package org.egov.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.egov.web.enums.CollectionType;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

/**
 * BillDetail
 */
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2019-02-25T15:07:36.183+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillDetail {
        @JsonProperty("id")
        private String id = null;

        @JsonProperty("bill")
        private String bill = null;

        @JsonProperty("demandId")
        private String demandId = null;

        @JsonProperty("billDate")
        private Long billDate = null;

        @JsonProperty("billNumber")
        private String billNumber = null;

        @JsonProperty("consumerCode")
        private String consumerCode = null;

        @JsonProperty("consumerType")
        private String consumerType = null;

        @JsonProperty("billDescription")
        private String billDescription = null;

        @JsonProperty("minimumAmount")
        private BigDecimal minimumAmount = null;

        @JsonProperty("totalAmount")
        private BigDecimal totalAmount = null;

        @JsonProperty("collectedAmount")
        private BigDecimal collectedAmount = null;

        @JsonProperty("amountPaid")
        private Double amountPaid = null;

        @JsonProperty("collectionModesNotAllowed")
        @Valid
        private List<String> collectionModesNotAllowed = null;

        @JsonProperty("event")
        private String event = null;

        @JsonProperty("receiptNumber")
        private String receiptNumber = null;

        @JsonProperty("receiptDate")
        private Long receiptDate = null;

        @JsonProperty("channel")
        private String channel = null;

        @JsonProperty("fund")
        private String fund = null;

        @JsonProperty("department")
        private String department = null;

        @JsonProperty("function")
        private String function = null;

        @JsonProperty("displayMessage")
        private String displayMessage = null;

        @JsonProperty("tenantId")
        private String tenantId = null;

        @JsonProperty("businessService")
        private String businessService = null;

        @JsonProperty("callBackForApportioning")
        private Boolean callBackForApportioning = null;

        @JsonProperty("partPaymentAllowed")
        private Boolean partPaymentAllowed = null;

        @JsonProperty("manualReceiptNumber")
        private String manualReceiptNumber = null;

        @JsonProperty("manualReceiptDate")
        private Long manualReceiptDate = null;

        @JsonProperty("stateId")
        private Integer stateId = null;

        @JsonProperty("billAccountDetails")
        @Valid
        private List<BillAccountDetail> billAccountDetails = null;

        @JsonProperty("fromPeriod")
        private Long fromPeriod = null;

        @JsonProperty("toPeriod")
        private Long toPeriod = null;

        @JsonProperty("additionalDetails")
        private Object additionalDetails = null;

        @NotNull
        @JsonProperty("collectionType")
        private CollectionType collectionType = null;


        public BillDetail addCollectionModesNotAllowedItem(String collectionModesNotAllowedItem) {
            if (this.collectionModesNotAllowed == null) {
            this.collectionModesNotAllowed = new ArrayList<>();
            }
        this.collectionModesNotAllowed.add(collectionModesNotAllowedItem);
        return this;
        }

        public BillDetail addBillAccountDetailsItem(BillAccountDetail billAccountDetailItem) {
            if (this.billAccountDetails == null) {
            this.billAccountDetails = new ArrayList<>();
            }
        this.billAccountDetails.add(billAccountDetailItem);
        return this;
        }

}

