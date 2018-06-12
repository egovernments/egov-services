package org.egov.pt.calculator.web.models.property;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * BillingSlab
 */
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2018-05-31T14:59:52.408+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillingSlab   {
        @JsonProperty("tenantId")
        private String tenantId = null;

        @JsonProperty("id")
        private String id = null;

        @JsonProperty("propertyType")
        private String propertyType = null;

        @JsonProperty("propertySubType")
        private String propertySubType = null;

        @JsonProperty("usageCategoryMajor")
        private String usageCategoryMajor = null;

        @JsonProperty("usageCategoryMinor")
        private String usageCategoryMinor = null;

        @JsonProperty("usageCategorySubMinor")
        private String usageCategorySubMinor = null;

        @JsonProperty("usageCategoryDetail")
        private String usageCategoryDetail = null;

        @JsonProperty("ownerShipCategory")
        private String ownerShipCategory = null;

        @JsonProperty("subOwnerShipCategory")
        private String subOwnerShipCategory = null;

        @JsonProperty("areaType")
        private String areaType = null;

        @JsonProperty("fromPlotSize")
        private Double fromPlotSize = null;

        @JsonProperty("toPlotSize")
        private Double toPlotSize = null;
        
        @JsonProperty("occupancyType")
        private String occupancyType = null;
        
        @JsonProperty("fromFloor")
        private Double fromFloor = null;

        @JsonProperty("toFloor")
        private Double toFloor = null;

        @JsonProperty("unitRate")
        private Double unitRate = null;
        
        @JsonProperty("auditDetails")
        private AuditDetails auditDetails;


}

