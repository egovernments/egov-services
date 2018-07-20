package org.egov.pt.calculator.web.models.property;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
@ToString
@EqualsAndHashCode
public class Unit   {
        @JsonProperty("id")
        private String id;

        @JsonProperty("tenantId")
        private String tenantId;

        @NotEmpty
        @JsonProperty("floorNo")
        private String floorNo;

        @JsonProperty("unitType")
        private String unitType;

        @NotNull
        @JsonProperty("unitArea")
        private Float unitArea;

        @NotEmpty
        @JsonProperty("usageCategoryMajor")
        private String usageCategoryMajor;

        @NotEmpty
        @JsonProperty("usageCategoryMinor")
        private String usageCategoryMinor;

        @NotEmpty
        @JsonProperty("usageCategorySubMinor")
        private String usageCategorySubMinor;

        @NotEmpty
        @JsonProperty("usageCategoryDetail")
        private String usageCategoryDetail;

        @NotEmpty
        @JsonProperty("occupancyType")
        private String occupancyType;

        @JsonProperty("occupancyDate")
        private Long occupancyDate;

        @JsonProperty("constructionType")
        private String constructionType;

        @JsonProperty("constructionSubType")
        private String constructionSubType;

        @JsonProperty("arv")
        private BigDecimal arv;
}

