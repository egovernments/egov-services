package org.egov.pt.web.models;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(exclude={"usage"})
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

        @JsonProperty("usageCategoryMajor")
        private String usageCategoryMajor;

        @JsonProperty("usageCategoryMinor")
        private String usageCategoryMinor;

        @JsonProperty("usageCategorySubMinor")
        private String usageCategorySubMinor;

        @JsonProperty("usageCategoryDetail")
        private String usageCategoryDetail;

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

