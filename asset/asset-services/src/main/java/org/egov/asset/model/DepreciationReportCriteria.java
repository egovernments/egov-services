package org.egov.asset.model;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
@Builder
public class DepreciationReportCriteria {

    @JsonProperty("tenantId")
    @NotNull
    private String tenantId;

    @JsonProperty("assetCategoryType")
    @NotNull
    private String assetCategoryType;

    @JsonProperty("assetCategoryName")
    @NotNull
    private String assetCategoryName;

    @JsonProperty("assetName")
    private String assetName;

    @JsonProperty("assetCategoryTree")
    private String assetCategoryTree;

}
