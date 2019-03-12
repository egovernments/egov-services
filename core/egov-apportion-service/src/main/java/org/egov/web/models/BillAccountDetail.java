package org.egov.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

import org.egov.web.models.enums.Purpose;
import org.springframework.validation.annotation.Validated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

/**
 * BillAccountDetail
 */
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2019-02-25T15:07:36.183+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillAccountDetail {
        @JsonProperty("id")
        private String id = null;

        @JsonProperty("glcode")
        private String glcode = null;

        @JsonProperty("order")
        private Integer order = null;

        @JsonProperty("amount")
        private BigDecimal amount = null;

        @JsonProperty("adjustedAmount")
        private BigDecimal adjustedAmount;

        @JsonProperty("isActualDemand")
        private Boolean isActualDemand = null;

        @JsonProperty("tenantId")
        private String tenantId = null;

        @JsonProperty("billDetail")
        private String billDetail = null;

        @JsonProperty("demandDetailId")
        private String demandDetailId = null;

        @JsonProperty("taxHeadCode")
        private String taxHeadCode = null;

        @JsonProperty("purpose")
        private Purpose purpose;

        @JsonProperty("additionalDetails")
        private Object additionalDetails = null;



}

