package org.egov.pt.calculator.web.models;

import org.egov.pt.calculator.web.models.property.Property;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * CalulationCriteria
 */
@Validated

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CalculationCriteria   {
        @JsonProperty("property")
        private Property property;

        @JsonProperty("assesmentNumber")
        private String assesmentNumber;

        @JsonProperty("assesmentYear")
        private String assesmentYear;

        @JsonProperty("oldAssesmentNumber")
        private String oldAssesmentNumber;

        @JsonProperty("tenantId")
        private String tenantId;


}

