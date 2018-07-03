package org.egov.pt.calculator.web.models;

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
        private Double totalAmount;
        
        private Double taxAmount; 

        @JsonProperty("penalty")
        private Double penalty;

        @JsonProperty("exemption")
        private Double exemption;

        @JsonProperty("rebate")
        private Double rebate;

        @JsonProperty("fromDate")
        private Long fromDate;

        @JsonProperty("toDate")
        private Long toDate;

        @JsonProperty("tenantId")
        private String tenantId;

        List<TaxHeadEstimate> taxHeadEstimates;
}

