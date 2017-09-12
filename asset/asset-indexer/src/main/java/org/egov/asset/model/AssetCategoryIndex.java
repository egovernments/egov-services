package org.egov.asset.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class AssetCategoryIndex {
    private String tenantId;
    private Long id;
    private String name;
    private String code;
    private String assetCategoryType;
    private Long parent;
    private String depreciationMethod;
    private Boolean isAssetAllow;
    private Long assetAccount;
    private Long accumulatedDepreciationAccount;
    private Long revaluationReserveAccount;
    private Long depreciationExpenseAccount;
    private Long unitOfMeasurement;
    private String version;
    private Double depreciationRate;
    private String assetFieldsDefination;

    private String cityName;
    private String localName;
    private String districtCode;
    private String districtName;
    private String regionName;
    private String ulbGrade;
}
