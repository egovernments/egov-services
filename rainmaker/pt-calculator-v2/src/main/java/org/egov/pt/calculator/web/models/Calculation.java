package org.egov.pt.calculator.web.models;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Calculation
 */
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2018-05-14T00:55:55.623+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Calculation   {
        @JsonProperty("connectionNumber")
        private String connectionNumber;

        @JsonProperty("totalAmount")
        private Double totalAmount;

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
}

