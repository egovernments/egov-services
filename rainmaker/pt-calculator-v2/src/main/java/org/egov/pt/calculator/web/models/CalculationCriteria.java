package org.egov.pt.calculator.web.models;

import org.egov.pt.calculator.web.models.property.Property;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * CalulationCriteria
 */
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2018-05-14T00:55:55.623+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CalculationCriteria   {
        @JsonProperty("property")
        private Property property = null;

        @JsonProperty("assesmentNumber")
        private String assesmentNumber = null;

        @JsonProperty("assesmentYear")
        private String assesmentYear = null;

        @JsonProperty("oldAssesmentNumber")
        private String oldAssesmentNumber = null;

        @JsonProperty("tenantId")
        private String tenantId = null;


}

