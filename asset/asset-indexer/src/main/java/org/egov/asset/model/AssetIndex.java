package org.egov.asset.model;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class AssetIndex {

    private String tenantId;
    private Long assetId;
    private String assetName;
    private String assetCode;

    private Long departmentId;
    private String departmentName;
    private String departmentCode;

    private Long assetCategoryId;
    private String assetCategoryName;
    private String assetCategoryCode;
    private String assetCategoryType;
    private Long assetCategoryparentId;
    private String assetCategoryparentName;
    private Double assetCategoryDepreciationRate;
    private String depreciationMethod;
    private String assetCategoryVersion;

    private String assetDetails;
    private String modeOfAcquisition;
    private String status;
    private String description;
    private Date dateOfCreation;

    private Long locality;
    private String localityName;
    private Long zone;
    private String zoneName;
    private Long revenueWard;
    private String revenueWardName;
    private Long block;
    private String blockName;
    private Long street;
    private String streetName;
    private Long electionWard;
    private String electionWardName;
    private String doorNo;
    private Long pinCode;

    private String remarks;
    private String length;
    private String width;
    private String totalArea;
    private String assetAttributes;

    private Boolean enableYearWiseDepreciation;
    private Double depreciationRate;
    private String yearWiseDepreciation;
    private BigDecimal grossValue;
    private BigDecimal accumulatedDepreciation;
    private Long assetReference;
    private String version;

    private String cityName;
    private String localName;
    private String districtCode;
    private String districtName;
    private String regionName;
    private String ulbGrade;

}
