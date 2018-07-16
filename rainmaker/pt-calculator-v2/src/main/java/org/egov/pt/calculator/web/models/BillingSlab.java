package org.egov.pt.calculator.web.models;

import org.egov.pt.calculator.web.models.property.AuditDetails;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * BillingSlab
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillingSlab   {
        @JsonProperty("tenantId")
        private String tenantId;

        @JsonProperty("id")
        private String id;

        @JsonProperty("propertyType")
        private String propertyType;

        @JsonProperty("propertySubType")
        private String propertySubType;

        @JsonProperty("usageCategoryMajor")
        private String usageCategoryMajor;

        @JsonProperty("usageCategoryMinor")
        private String usageCategoryMinor;

        @JsonProperty("usageCategorySubMinor")
        private String usageCategorySubMinor;

        @JsonProperty("usageCategoryDetail")
        private String usageCategoryDetail;

        @JsonProperty("ownerShipCategory")
        private String ownerShipCategory;

        @JsonProperty("subOwnerShipCategory")
        private String subOwnerShipCategory;

        @JsonProperty("areaType")
        private String areaType;

        @JsonProperty("fromPlotSize")
        private Double fromPlotSize;

        @JsonProperty("toPlotSize")
        private Double toPlotSize;
        
        @JsonProperty("occupancyType")
        private String occupancyType;
        
        @JsonProperty("fromFloor")
        private Double fromFloor;

        @JsonProperty("toFloor")
        private Double toFloor;

        @JsonProperty("unitRate")
        private Double unitRate;
        
        @JsonProperty("isPropertyMultiFloored")
        private Boolean isPropertyMultiFloored;
        
        @JsonProperty("auditDetails")
        private AuditDetails auditDetails;
}

