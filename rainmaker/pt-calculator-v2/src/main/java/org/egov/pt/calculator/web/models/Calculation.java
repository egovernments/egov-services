package org.egov.pt.calculator.web.models;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Calculation
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Calculation   {
	
        @JsonProperty("serviceNumber")
        private String serviceNumber;

        @JsonProperty("totalAmount")
        private BigDecimal totalAmount;
        
        private BigDecimal taxAmount; 

        @JsonProperty("penalty")
        private BigDecimal penalty;

        @JsonProperty("exemption")
        private BigDecimal exemption;

        @JsonProperty("rebate")
        private BigDecimal rebate;

        @JsonProperty("fromDate")
        private Long fromDate;

        @JsonProperty("toDate")
        private Long toDate;

        @JsonProperty("tenantId")
        private String tenantId;

        List<TaxHeadEstimate> taxHeadEstimates;
}

