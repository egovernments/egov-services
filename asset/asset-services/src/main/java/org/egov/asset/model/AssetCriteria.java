package org.egov.asset.model;

import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.egov.asset.model.enums.TransactionType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class AssetCriteria {

    private List<Long> id;
    private String assetCategoryType;
    private Set<Long> assetSubCategory;
    private String name;
    private String code;
    private Long assetCategory;
    private Long department;
    private String status;
    private Long locality;
    private Long zone;
    private Long revenueWard;
    private Long block;
    private Long street;
    private Long electionWard;
    private String doorNo;
    private Long pinCode;
    private Long assetReference;
    @NotNull
    private String tenantId;
    private Long size;
    private Long offset;

    private String description;
    private Double grossValue;
    private Double fromCapitalizedValue;
    private Double toCapitalizedValue;
    private Long assetCreatedFrom;
    private Long assetCreatedTo;
    private String assetCategoryName;

    private Long dateOfDepreciation;
    private TransactionType transaction;
    private Boolean isTransactionHistoryRequired;
}
