package org.egov.pt.calculator.web.models.property;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Unit
 */
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2018-05-11T14:12:44.497+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Unit   {
        @JsonProperty("id")
        private String id;

        @JsonProperty("tenantId")
        private String tenantId;

        @JsonProperty("floorNo")
        private String floorNo;

        @JsonProperty("unitType")
        private String unitType;

        @JsonProperty("unitArea")
        private Float unitArea;

        @JsonProperty("usage")
        @Valid
        private List<UnitUsage> usage;


        public Unit addUsageItem(UnitUsage usageItem) {
            if (this.usage == null) {
            this.usage = new ArrayList<>();
            }
        this.usage.add(usageItem);
        return this;
        }

}

