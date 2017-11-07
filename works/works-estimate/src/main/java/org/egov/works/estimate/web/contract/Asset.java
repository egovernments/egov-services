package org.egov.works.estimate.web.contract;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Hold the asset specific information.
 */
@ApiModel(description = "Hold the asset specific information.")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-03T07:36:47.547Z")

public class Asset   {
  @JsonProperty("anticipatedLife")
  private Long anticipatedLife = null;

  @JsonProperty("orderNumber")
  private String orderNumber = null;

  @JsonProperty("orderDate")
  private Long orderDate = null;

  @JsonProperty("wipReferenceNo")
  private String wipReferenceNo = null;

  @JsonProperty("acquiredFrom")
  private String acquiredFrom = null;

  @JsonProperty("warrantyAvailable")
  private Boolean warrantyAvailable = null;

  @JsonProperty("warrantyExpiryDate")
  private Long warrantyExpiryDate = null;

  @JsonProperty("defectLiabilityPeriod")
  private DefectLiability defectLiabilityPeriod = null;

  @JsonProperty("securityDepositRetained")
  private BigDecimal securityDepositRetained = null;

  @JsonProperty("securityDepositRealized")
  private BigDecimal securityDepositRealized = null;

  @JsonProperty("acquisitionDate")
  private Long acquisitionDate = null;

  @JsonProperty("originalValue")
  private BigDecimal originalValue = null;

  @JsonProperty("assetAccount")
  private String assetAccount = null;

  @JsonProperty("accumulatedDepreciationAccount")
  private String accumulatedDepreciationAccount = null;

  @JsonProperty("revaluationReserveAccount")
  private String revaluationReserveAccount = null;

  @JsonProperty("depreciationExpenseAccount")
  private String depreciationExpenseAccount = null;

  @JsonProperty("titleDocumentsAvalable")
  private List<String> titleDocumentsAvalable = null;

  @JsonProperty("usage")
  private String usage = null;

  @JsonProperty("locationDetails")
  private Location locationDetails = null;

  @JsonProperty("length")
  private Double length = null;

  @JsonProperty("width")
  private Double width = null;

  @JsonProperty("height")
  private Double height = null;

  @JsonProperty("totalArea")
  private Double totalArea = null;

  @JsonProperty("plinthArea")
  private Double plinthArea = null;

  @JsonProperty("address")
  private String address = null;

  @JsonProperty("longitude")
  private Double longitude = null;

  @JsonProperty("latitude")
  private Double latitude = null;

  @JsonProperty("floors")
  private Long floors = null;

  @JsonProperty("landSurveyNo")
  private String landSurveyNo = null;

  @JsonProperty("cubicContents")
  private String cubicContents = null;

  @JsonProperty("quantity")
  private Long quantity = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("code")
  private String code = null;

  @JsonProperty("oldCode")
  private String oldCode = null;

  @JsonProperty("departmentCode")
  private String departmentCode = null;

  @JsonProperty("assetCategory")
  private AssetCategory assetCategory = null;

  /**
   * asset mode of acquistion enumeration.
   */
  public enum ModeOfAcquisitionEnum {
    ACQUIRED("ACQUIRED"),
    
    CONSTRUCTION("CONSTRUCTION"),
    
    PURCHASE("PURCHASE"),
    
    TENDER("TENDER"),
    
    DONATION("DONATION");

    private String value;

    ModeOfAcquisitionEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static ModeOfAcquisitionEnum fromValue(String text) {
      for (ModeOfAcquisitionEnum b : ModeOfAcquisitionEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("modeOfAcquisition")
  private ModeOfAcquisitionEnum modeOfAcquisition = null;

  @JsonProperty("status")
  private String status = null;

  @JsonProperty("grossValue")
  private BigDecimal grossValue = null;

  @JsonProperty("accumulatedDepreciation")
  private BigDecimal accumulatedDepreciation = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("dateOfCreation")
  private Long dateOfCreation = null;

  @JsonProperty("remarks")
  private String remarks = null;

  @JsonProperty("version")
  private String version = null;

  @JsonProperty("assetReference")
  private Long assetReference = null;

  @JsonProperty("enableYearWiseDepreciation")
  private Boolean enableYearWiseDepreciation = null;

  @JsonProperty("assetAttributes")
  private List<Attributes> assetAttributes = null;

  @JsonProperty("depreciationRate")
  private Double depreciationRate = null;

  @JsonProperty("yearWiseDepreciationRate")
  private List<YearWiseDepreciation> yearWiseDepreciationRate = null;

  public Asset anticipatedLife(Long anticipatedLife) {
    this.anticipatedLife = anticipatedLife;
    return this;
  }

   /**
   * Anticipated life of asset.
   * @return anticipatedLife
  **/
  @ApiModelProperty(required = true, value = "Anticipated life of asset.")
  //@NotNull


  public Long getAnticipatedLife() {
    return anticipatedLife;
  }

  public void setAnticipatedLife(Long anticipatedLife) {
    this.anticipatedLife = anticipatedLife;
  }

  public Asset orderNumber(String orderNumber) {
    this.orderNumber = orderNumber;
    return this;
  }

   /**
   * unique id of the respective order.
   * @return orderNumber
  **/
  @ApiModelProperty(value = "unique id of the respective order.")


  public String getOrderNumber() {
    return orderNumber;
  }

  public void setOrderNumber(String orderNumber) {
    this.orderNumber = orderNumber;
  }

  public Asset orderDate(Long orderDate) {
    this.orderDate = orderDate;
    return this;
  }

   /**
   * Date of the respective order.
   * @return orderDate
  **/
  @ApiModelProperty(value = "Date of the respective order.")


  public Long getOrderDate() {
    return orderDate;
  }

  public void setOrderDate(Long orderDate) {
    this.orderDate = orderDate;
  }

  public Asset wipReferenceNo(String wipReferenceNo) {
    this.wipReferenceNo = wipReferenceNo;
    return this;
  }

   /**
   * Ref No of WIP(work in progress) Register.
   * @return wipReferenceNo
  **/
  @ApiModelProperty(required = true, value = "Ref No of WIP(work in progress) Register.")
  //@NotNull


  public String getWipReferenceNo() {
    return wipReferenceNo;
  }

  public void setWipReferenceNo(String wipReferenceNo) {
    this.wipReferenceNo = wipReferenceNo;
  }

  public Asset acquiredFrom(String acquiredFrom) {
    this.acquiredFrom = acquiredFrom;
    return this;
  }

   /**
   * From whom acquired should be entered.
   * @return acquiredFrom
  **/
  @ApiModelProperty(value = "From whom acquired should be entered.")


  public String getAcquiredFrom() {
    return acquiredFrom;
  }

  public void setAcquiredFrom(String acquiredFrom) {
    this.acquiredFrom = acquiredFrom;
  }

  public Asset warrantyAvailable(Boolean warrantyAvailable) {
    this.warrantyAvailable = warrantyAvailable;
    return this;
  }

   /**
   * is Warranty available for the given asset.
   * @return warrantyAvailable
  **/
  @ApiModelProperty(value = "is Warranty available for the given asset.")


  public Boolean getWarrantyAvailable() {
    return warrantyAvailable;
  }

  public void setWarrantyAvailable(Boolean warrantyAvailable) {
    this.warrantyAvailable = warrantyAvailable;
  }

  public Asset warrantyExpiryDate(Long warrantyExpiryDate) {
    this.warrantyExpiryDate = warrantyExpiryDate;
    return this;
  }

   /**
   * Date of the expiry of warranty.
   * @return warrantyExpiryDate
  **/
  @ApiModelProperty(value = "Date of the expiry of warranty.")


  public Long getWarrantyExpiryDate() {
    return warrantyExpiryDate;
  }

  public void setWarrantyExpiryDate(Long warrantyExpiryDate) {
    this.warrantyExpiryDate = warrantyExpiryDate;
  }

  public Asset defectLiabilityPeriod(DefectLiability defectLiabilityPeriod) {
    this.defectLiabilityPeriod = defectLiabilityPeriod;
    return this;
  }

   /**
   * Get defectLiabilityPeriod
   * @return defectLiabilityPeriod
  **/
  @ApiModelProperty(value = "")

  @Valid

  public DefectLiability getDefectLiabilityPeriod() {
    return defectLiabilityPeriod;
  }

  public void setDefectLiabilityPeriod(DefectLiability defectLiabilityPeriod) {
    this.defectLiabilityPeriod = defectLiabilityPeriod;
  }

  public Asset securityDepositRetained(BigDecimal securityDepositRetained) {
    this.securityDepositRetained = securityDepositRetained;
    return this;
  }

   /**
   * value of the security deposit retained.
   * @return securityDepositRetained
  **/
  @ApiModelProperty(value = "value of the security deposit retained.")

  @Valid

  public BigDecimal getSecurityDepositRetained() {
    return securityDepositRetained;
  }

  public void setSecurityDepositRetained(BigDecimal securityDepositRetained) {
    this.securityDepositRetained = securityDepositRetained;
  }

  public Asset securityDepositRealized(BigDecimal securityDepositRealized) {
    this.securityDepositRealized = securityDepositRealized;
    return this;
  }

   /**
   * value of the security deposit realized.
   * @return securityDepositRealized
  **/
  @ApiModelProperty(value = "value of the security deposit realized.")

  @Valid

  public BigDecimal getSecurityDepositRealized() {
    return securityDepositRealized;
  }

  public void setSecurityDepositRealized(BigDecimal securityDepositRealized) {
    this.securityDepositRealized = securityDepositRealized;
  }

  public Asset acquisitionDate(Long acquisitionDate) {
    this.acquisitionDate = acquisitionDate;
    return this;
  }

   /**
   * Date of the Acquisition.
   * @return acquisitionDate
  **/
  @ApiModelProperty(required = true, value = "Date of the Acquisition.")
  //@NotNull


  public Long getAcquisitionDate() {
    return acquisitionDate;
  }

  public void setAcquisitionDate(Long acquisitionDate) {
    this.acquisitionDate = acquisitionDate;
  }

  public Asset originalValue(BigDecimal originalValue) {
    this.originalValue = originalValue;
    return this;
  }

   /**
   * value of the asset when it was created originally, will be same as gross value if the asset is being created from the system.
   * @return originalValue
  **/
  @ApiModelProperty(required = true, value = "value of the asset when it was created originally, will be same as gross value if the asset is being created from the system.")
  //@NotNull

  @Valid

  public BigDecimal getOriginalValue() {
    return originalValue;
  }

  public void setOriginalValue(BigDecimal originalValue) {
    this.originalValue = originalValue;
  }

  public Asset assetAccount(String assetAccount) {
    this.assetAccount = assetAccount;
    return this;
  }

   /**
   * Options are from the  chart of account master for the account code purpose \"Fixed Asset\".
   * @return assetAccount
  **/
  @ApiModelProperty(required = true, value = "Options are from the  chart of account master for the account code purpose \"Fixed Asset\".")
  //@NotNull


  public String getAssetAccount() {
    return assetAccount;
  }

  public void setAssetAccount(String assetAccount) {
    this.assetAccount = assetAccount;
  }

  public Asset accumulatedDepreciationAccount(String accumulatedDepreciationAccount) {
    this.accumulatedDepreciationAccount = accumulatedDepreciationAccount;
    return this;
  }

   /**
   * Options are from the  chart of account master for the account code purpose \"Accumulated Depreciation\".
   * @return accumulatedDepreciationAccount
  **/
  @ApiModelProperty(required = true, value = "Options are from the  chart of account master for the account code purpose \"Accumulated Depreciation\".")
 // @NotNull


  public String getAccumulatedDepreciationAccount() {
    return accumulatedDepreciationAccount;
  }

  public void setAccumulatedDepreciationAccount(String accumulatedDepreciationAccount) {
    this.accumulatedDepreciationAccount = accumulatedDepreciationAccount;
  }

  public Asset revaluationReserveAccount(String revaluationReserveAccount) {
    this.revaluationReserveAccount = revaluationReserveAccount;
    return this;
  }

   /**
   * Options are from the  chart of account master for the account code purpose \"Revaluation Reserve Account\".
   * @return revaluationReserveAccount
  **/
  @ApiModelProperty(required = true, value = "Options are from the  chart of account master for the account code purpose \"Revaluation Reserve Account\".")
  //@NotNull


  public String getRevaluationReserveAccount() {
    return revaluationReserveAccount;
  }

  public void setRevaluationReserveAccount(String revaluationReserveAccount) {
    this.revaluationReserveAccount = revaluationReserveAccount;
  }

  public Asset depreciationExpenseAccount(String depreciationExpenseAccount) {
    this.depreciationExpenseAccount = depreciationExpenseAccount;
    return this;
  }

   /**
   * Options are from the  chart of account master for the account code purpose \"Depreciation Expense Account\".
   * @return depreciationExpenseAccount
  **/
  @ApiModelProperty(required = true, value = "Options are from the  chart of account master for the account code purpose \"Depreciation Expense Account\".")
 // @NotNull


  public String getDepreciationExpenseAccount() {
    return depreciationExpenseAccount;
  }

  public void setDepreciationExpenseAccount(String depreciationExpenseAccount) {
    this.depreciationExpenseAccount = depreciationExpenseAccount;
  }

  public Asset titleDocumentsAvalable(List<String> titleDocumentsAvalable) {
    this.titleDocumentsAvalable = titleDocumentsAvalable;
    return this;
  }

  public Asset addTitleDocumentsAvalableItem(String titleDocumentsAvalableItem) {
    if (this.titleDocumentsAvalable == null) {
      this.titleDocumentsAvalable = new ArrayList<String>();
    }
    this.titleDocumentsAvalable.add(titleDocumentsAvalableItem);
    return this;
  }

   /**
   * names of the title documents available for the particular asset.
   * @return titleDocumentsAvalable
  **/
  @ApiModelProperty(value = "names of the title documents available for the particular asset.")


  public List<String> getTitleDocumentsAvalable() {
    return titleDocumentsAvalable;
  }

  public void setTitleDocumentsAvalable(List<String> titleDocumentsAvalable) {
    this.titleDocumentsAvalable = titleDocumentsAvalable;
  }

  public Asset usage(String usage) {
    this.usage = usage;
    return this;
  }

   /**
   * usage.
   * @return usage
  **/
  @ApiModelProperty(value = "usage.")


  public String getUsage() {
    return usage;
  }

  public void setUsage(String usage) {
    this.usage = usage;
  }

  public Asset locationDetails(Location locationDetails) {
    this.locationDetails = locationDetails;
    return this;
  }

   /**
   * Get locationDetails
   * @return locationDetails
  **/
  @ApiModelProperty(value = "")

  @Valid

  public Location getLocationDetails() {
    return locationDetails;
  }

  public void setLocationDetails(Location locationDetails) {
    this.locationDetails = locationDetails;
  }

  public Asset length(Double length) {
    this.length = length;
    return this;
  }

   /**
   * Length of the Land and Shop assets.
   * @return length
  **/
  @ApiModelProperty(value = "Length of the Land and Shop assets.")


  public Double getLength() {
    return length;
  }

  public void setLength(Double length) {
    this.length = length;
  }

  public Asset width(Double width) {
    this.width = width;
    return this;
  }

   /**
   * Width of the Land and Shop assets.
   * @return width
  **/
  @ApiModelProperty(value = "Width of the Land and Shop assets.")


  public Double getWidth() {
    return width;
  }

  public void setWidth(Double width) {
    this.width = width;
  }

  public Asset height(Double height) {
    this.height = height;
    return this;
  }

   /**
   * Height of the Land and Shop assets.
   * @return height
  **/
  @ApiModelProperty(value = "Height of the Land and Shop assets.")


  public Double getHeight() {
    return height;
  }

  public void setHeight(Double height) {
    this.height = height;
  }

  public Asset totalArea(Double totalArea) {
    this.totalArea = totalArea;
    return this;
  }

   /**
   * Total area of Land on which the asset is located.
   * @return totalArea
  **/
  @ApiModelProperty(value = "Total area of Land on which the asset is located.")


  public Double getTotalArea() {
    return totalArea;
  }

  public void setTotalArea(Double totalArea) {
    this.totalArea = totalArea;
  }

  public Asset plinthArea(Double plinthArea) {
    this.plinthArea = plinthArea;
    return this;
  }

   /**
   * area of Land on which the asset is constructed.
   * @return plinthArea
  **/
  @ApiModelProperty(value = "area of Land on which the asset is constructed.")


  public Double getPlinthArea() {
    return plinthArea;
  }

  public void setPlinthArea(Double plinthArea) {
    this.plinthArea = plinthArea;
  }

  public Asset address(String address) {
    this.address = address;
    return this;
  }

   /**
   * address as entered by the user.
   * @return address
  **/
  @ApiModelProperty(required = true, value = "address as entered by the user.")
  //@NotNull


  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public Asset longitude(Double longitude) {
    this.longitude = longitude;
    return this;
  }

   /**
   * longitude coordinate of asset.
   * @return longitude
  **/
  @ApiModelProperty(value = "longitude coordinate of asset.")


  public Double getLongitude() {
    return longitude;
  }

  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }

  public Asset latitude(Double latitude) {
    this.latitude = latitude;
    return this;
  }

   /**
   * latitude coordinate of asset.
   * @return latitude
  **/
  @ApiModelProperty(value = "latitude coordinate of asset.")


  public Double getLatitude() {
    return latitude;
  }

  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  public Asset floors(Long floors) {
    this.floors = floors;
    return this;
  }

   /**
   * No of floors available in the building.
   * @return floors
  **/
  @ApiModelProperty(value = "No of floors available in the building.")


  public Long getFloors() {
    return floors;
  }

  public void setFloors(Long floors) {
    this.floors = floors;
  }

  public Asset landSurveyNo(String landSurveyNo) {
    this.landSurveyNo = landSurveyNo;
    return this;
  }

   /**
   * Survey No of land on which structure is located.
   * @return landSurveyNo
  **/
  @ApiModelProperty(value = "Survey No of land on which structure is located.")


  public String getLandSurveyNo() {
    return landSurveyNo;
  }

  public void setLandSurveyNo(String landSurveyNo) {
    this.landSurveyNo = landSurveyNo;
  }

  public Asset cubicContents(String cubicContents) {
    this.cubicContents = cubicContents;
    return this;
  }

   /**
   * cubic contents.
   * @return cubicContents
  **/
  @ApiModelProperty(value = "cubic contents.")


  public String getCubicContents() {
    return cubicContents;
  }

  public void setCubicContents(String cubicContents) {
    this.cubicContents = cubicContents;
  }

  public Asset quantity(Long quantity) {
    this.quantity = quantity;
    return this;
  }

   /**
   * No of assets being created.
   * @return quantity
  **/
  @ApiModelProperty(value = "No of assets being created.")


  public Long getQuantity() {
    return quantity;
  }

  public void setQuantity(Long quantity) {
    this.quantity = quantity;
  }

  public Asset tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Unique id for ulb.
   * @return tenantId
  **/
  @ApiModelProperty(value = "Unique id for ulb.")


  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public Asset id(Long id) {
    this.id = id;
    return this;
  }

   /**
   * Unique id for the asset. This will be auto generated.
   * @return id
  **/
  @ApiModelProperty(value = "Unique id for the asset. This will be auto generated.")


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Asset name(String name) {
    this.name = name;
    return this;
  }

   /**
   * Name of the Asset.
   * @return name
  **/
  @ApiModelProperty(required = true, value = "Name of the Asset.")
  //@NotNull


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Asset code(String code) {
    this.code = code;
    return this;
  }

   /**
   * Unique code for the asset. This will be auto generated. The format for the asset code will be given by the client in case of auto generation of asset code.
   * @return code
  **/
  @ApiModelProperty(value = "Unique code for the asset. This will be auto generated. The format for the asset code will be given by the client in case of auto generation of asset code.")


  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Asset oldCode(String oldCode) {
    this.oldCode = oldCode;
    return this;
  }

   /**
   * Old Asset ID should be entered for Asset Older than go live Date of this module.
   * @return oldCode
  **/
  @ApiModelProperty(value = "Old Asset ID should be entered for Asset Older than go live Date of this module.")


  public String getOldCode() {
    return oldCode;
  }

  public void setOldCode(String oldCode) {
    this.oldCode = oldCode;
  }

  public Asset departmentCode(String departmentCode) {
    this.departmentCode = departmentCode;
    return this;
  }

   /**
   * code of the department to which the asset belongs
   * @return departmentCode
  **/
  @ApiModelProperty(required = true, value = "code of the department to which the asset belongs")
  //@NotNull


  public String getDepartmentCode() {
    return departmentCode;
  }

  public void setDepartmentCode(String departmentCode) {
    this.departmentCode = departmentCode;
  }

  public Asset assetCategory(AssetCategory assetCategory) {
    this.assetCategory = assetCategory;
    return this;
  }

   /**
   * Get assetCategory
   * @return assetCategory
  **/
  @ApiModelProperty(required = true, value = "")
  //@NotNull

  @Valid

  public AssetCategory getAssetCategory() {
    return assetCategory;
  }

  public void setAssetCategory(AssetCategory assetCategory) {
    this.assetCategory = assetCategory;
  }

  public Asset modeOfAcquisition(ModeOfAcquisitionEnum modeOfAcquisition) {
    this.modeOfAcquisition = modeOfAcquisition;
    return this;
  }

   /**
   * asset mode of acquistion enumeration.
   * @return modeOfAcquisition
  **/
  @ApiModelProperty(value = "asset mode of acquistion enumeration.")


  public ModeOfAcquisitionEnum getModeOfAcquisition() {
    return modeOfAcquisition;
  }

  public void setModeOfAcquisition(ModeOfAcquisitionEnum modeOfAcquisition) {
    this.modeOfAcquisition = modeOfAcquisition;
  }

  public Asset status(String status) {
    this.status = status;
    return this;
  }

   /**
   * asset status from assetstatus master for Object name \"Asset Master\".
   * @return status
  **/
  @ApiModelProperty(value = "asset status from assetstatus master for Object name \"Asset Master\".")


  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Asset grossValue(BigDecimal grossValue) {
    this.grossValue = grossValue;
    return this;
  }

   /**
   * current written down value of the asset when being created or brought in to the system. This will be same as the original value for the newly created assets.
   * @return grossValue
  **/
  @ApiModelProperty(required = true, value = "current written down value of the asset when being created or brought in to the system. This will be same as the original value for the newly created assets.")
  //@NotNull

  @Valid

  public BigDecimal getGrossValue() {
    return grossValue;
  }

  public void setGrossValue(BigDecimal grossValue) {
    this.grossValue = grossValue;
  }

  public Asset accumulatedDepreciation(BigDecimal accumulatedDepreciation) {
    this.accumulatedDepreciation = accumulatedDepreciation;
    return this;
  }

   /**
   * gross value of the asset, applicable only if the status is Capitalized
   * @return accumulatedDepreciation
  **/
  @ApiModelProperty(value = "gross value of the asset, applicable only if the status is Capitalized")

  @Valid

  public BigDecimal getAccumulatedDepreciation() {
    return accumulatedDepreciation;
  }

  public void setAccumulatedDepreciation(BigDecimal accumulatedDepreciation) {
    this.accumulatedDepreciation = accumulatedDepreciation;
  }

  public Asset description(String description) {
    this.description = description;
    return this;
  }

   /**
   * description of asset
   * @return description
  **/
  @ApiModelProperty(required = true, value = "description of asset")
  //@NotNull


  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Asset dateOfCreation(Long dateOfCreation) {
    this.dateOfCreation = dateOfCreation;
    return this;
  }

   /**
   * Date of asset creation.
   * @return dateOfCreation
  **/
  @ApiModelProperty(required = true, value = "Date of asset creation.")
  //@NotNull


  public Long getDateOfCreation() {
    return dateOfCreation;
  }

  public void setDateOfCreation(Long dateOfCreation) {
    this.dateOfCreation = dateOfCreation;
  }

  public Asset remarks(String remarks) {
    this.remarks = remarks;
    return this;
  }

   /**
   * asset remarks.
   * @return remarks
  **/
  @ApiModelProperty(value = "asset remarks.")


  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public Asset version(String version) {
    this.version = version;
    return this;
  }

   /**
   * version of the assetcategory for which asset is created.
   * @return version
  **/
  @ApiModelProperty(value = "version of the assetcategory for which asset is created.")


  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public Asset assetReference(Long assetReference) {
    this.assetReference = assetReference;
    return this;
  }

   /**
   * Parent asset id of an asset.
   * @return assetReference
  **/
  @ApiModelProperty(value = "Parent asset id of an asset.")


  public Long getAssetReference() {
    return assetReference;
  }

  public void setAssetReference(Long assetReference) {
    this.assetReference = assetReference;
  }

  public Asset enableYearWiseDepreciation(Boolean enableYearWiseDepreciation) {
    this.enableYearWiseDepreciation = enableYearWiseDepreciation;
    return this;
  }

   /**
   * Enable the year wise Depreciation for an asset. if the value is true then refer the Depreciation rate from yearWiseDepreciationRate object else refer to depreciationRate of asset.
   * @return enableYearWiseDepreciation
  **/
  @ApiModelProperty(value = "Enable the year wise Depreciation for an asset. if the value is true then refer the Depreciation rate from yearWiseDepreciationRate object else refer to depreciationRate of asset.")


  public Boolean getEnableYearWiseDepreciation() {
    return enableYearWiseDepreciation;
  }

  public void setEnableYearWiseDepreciation(Boolean enableYearWiseDepreciation) {
    this.enableYearWiseDepreciation = enableYearWiseDepreciation;
  }

  public Asset assetAttributes(List<Attributes> assetAttributes) {
    this.assetAttributes = assetAttributes;
    return this;
  }

  public Asset addAssetAttributesItem(Attributes assetAttributesItem) {
    if (this.assetAttributes == null) {
      this.assetAttributes = new ArrayList<Attributes>();
    }
    this.assetAttributes.add(assetAttributesItem);
    return this;
  }

   /**
   * Get assetAttributes
   * @return assetAttributes
  **/
  @ApiModelProperty(value = "")

  @Valid

  public List<Attributes> getAssetAttributes() {
    return assetAttributes;
  }

  public void setAssetAttributes(List<Attributes> assetAttributes) {
    this.assetAttributes = assetAttributes;
  }

  public Asset depreciationRate(Double depreciationRate) {
    this.depreciationRate = depreciationRate;
    return this;
  }

   /**
   * Asset level depreciation rate, if enableYearWiseDepreciation is false then check for depreciation rate from this field.
   * @return depreciationRate
  **/
  @ApiModelProperty(value = "Asset level depreciation rate, if enableYearWiseDepreciation is false then check for depreciation rate from this field.")


  public Double getDepreciationRate() {
    return depreciationRate;
  }

  public void setDepreciationRate(Double depreciationRate) {
    this.depreciationRate = depreciationRate;
  }

  public Asset yearWiseDepreciationRate(List<YearWiseDepreciation> yearWiseDepreciationRate) {
    this.yearWiseDepreciationRate = yearWiseDepreciationRate;
    return this;
  }

  public Asset addYearWiseDepreciationRateItem(YearWiseDepreciation yearWiseDepreciationRateItem) {
    if (this.yearWiseDepreciationRate == null) {
      this.yearWiseDepreciationRate = new ArrayList<YearWiseDepreciation>();
    }
    this.yearWiseDepreciationRate.add(yearWiseDepreciationRateItem);
    return this;
  }

   /**
   * Multiple rows can be specified for each  asset category, but there should not be an overlap in the rates for the same financial year.It is required if enableYearWiseDepreciation is true, if enableYearWiseDepreciation if false then refer Depreciation from depreciationRate of asset.
   * @return yearWiseDepreciationRate
  **/
  @ApiModelProperty(value = "Multiple rows can be specified for each  asset category, but there should not be an overlap in the rates for the same financial year.It is required if enableYearWiseDepreciation is true, if enableYearWiseDepreciation if false then refer Depreciation from depreciationRate of asset.")

  @Valid

  public List<YearWiseDepreciation> getYearWiseDepreciationRate() {
    return yearWiseDepreciationRate;
  }

  public void setYearWiseDepreciationRate(List<YearWiseDepreciation> yearWiseDepreciationRate) {
    this.yearWiseDepreciationRate = yearWiseDepreciationRate;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Asset asset = (Asset) o;
    return Objects.equals(this.anticipatedLife, asset.anticipatedLife) &&
        Objects.equals(this.orderNumber, asset.orderNumber) &&
        Objects.equals(this.orderDate, asset.orderDate) &&
        Objects.equals(this.wipReferenceNo, asset.wipReferenceNo) &&
        Objects.equals(this.acquiredFrom, asset.acquiredFrom) &&
        Objects.equals(this.warrantyAvailable, asset.warrantyAvailable) &&
        Objects.equals(this.warrantyExpiryDate, asset.warrantyExpiryDate) &&
        Objects.equals(this.defectLiabilityPeriod, asset.defectLiabilityPeriod) &&
        Objects.equals(this.securityDepositRetained, asset.securityDepositRetained) &&
        Objects.equals(this.securityDepositRealized, asset.securityDepositRealized) &&
        Objects.equals(this.acquisitionDate, asset.acquisitionDate) &&
        Objects.equals(this.originalValue, asset.originalValue) &&
        Objects.equals(this.assetAccount, asset.assetAccount) &&
        Objects.equals(this.accumulatedDepreciationAccount, asset.accumulatedDepreciationAccount) &&
        Objects.equals(this.revaluationReserveAccount, asset.revaluationReserveAccount) &&
        Objects.equals(this.depreciationExpenseAccount, asset.depreciationExpenseAccount) &&
        Objects.equals(this.titleDocumentsAvalable, asset.titleDocumentsAvalable) &&
        Objects.equals(this.usage, asset.usage) &&
        Objects.equals(this.locationDetails, asset.locationDetails) &&
        Objects.equals(this.length, asset.length) &&
        Objects.equals(this.width, asset.width) &&
        Objects.equals(this.height, asset.height) &&
        Objects.equals(this.totalArea, asset.totalArea) &&
        Objects.equals(this.plinthArea, asset.plinthArea) &&
        Objects.equals(this.address, asset.address) &&
        Objects.equals(this.longitude, asset.longitude) &&
        Objects.equals(this.latitude, asset.latitude) &&
        Objects.equals(this.floors, asset.floors) &&
        Objects.equals(this.landSurveyNo, asset.landSurveyNo) &&
        Objects.equals(this.cubicContents, asset.cubicContents) &&
        Objects.equals(this.quantity, asset.quantity) &&
        Objects.equals(this.tenantId, asset.tenantId) &&
        Objects.equals(this.id, asset.id) &&
        Objects.equals(this.name, asset.name) &&
        Objects.equals(this.code, asset.code) &&
        Objects.equals(this.oldCode, asset.oldCode) &&
        Objects.equals(this.departmentCode, asset.departmentCode) &&
        Objects.equals(this.assetCategory, asset.assetCategory) &&
        Objects.equals(this.modeOfAcquisition, asset.modeOfAcquisition) &&
        Objects.equals(this.status, asset.status) &&
        Objects.equals(this.grossValue, asset.grossValue) &&
        Objects.equals(this.accumulatedDepreciation, asset.accumulatedDepreciation) &&
        Objects.equals(this.description, asset.description) &&
        Objects.equals(this.dateOfCreation, asset.dateOfCreation) &&
        Objects.equals(this.remarks, asset.remarks) &&
        Objects.equals(this.version, asset.version) &&
        Objects.equals(this.assetReference, asset.assetReference) &&
        Objects.equals(this.enableYearWiseDepreciation, asset.enableYearWiseDepreciation) &&
        Objects.equals(this.assetAttributes, asset.assetAttributes) &&
        Objects.equals(this.depreciationRate, asset.depreciationRate) &&
        Objects.equals(this.yearWiseDepreciationRate, asset.yearWiseDepreciationRate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(anticipatedLife, orderNumber, orderDate, wipReferenceNo, acquiredFrom, warrantyAvailable, warrantyExpiryDate, defectLiabilityPeriod, securityDepositRetained, securityDepositRealized, acquisitionDate, originalValue, assetAccount, accumulatedDepreciationAccount, revaluationReserveAccount, depreciationExpenseAccount, titleDocumentsAvalable, usage, locationDetails, length, width, height, totalArea, plinthArea, address, longitude, latitude, floors, landSurveyNo, cubicContents, quantity, tenantId, id, name, code, oldCode, departmentCode, assetCategory, modeOfAcquisition, status, grossValue, accumulatedDepreciation, description, dateOfCreation, remarks, version, assetReference, enableYearWiseDepreciation, assetAttributes, depreciationRate, yearWiseDepreciationRate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Asset {\n");
    
    sb.append("    anticipatedLife: ").append(toIndentedString(anticipatedLife)).append("\n");
    sb.append("    orderNumber: ").append(toIndentedString(orderNumber)).append("\n");
    sb.append("    orderDate: ").append(toIndentedString(orderDate)).append("\n");
    sb.append("    wipReferenceNo: ").append(toIndentedString(wipReferenceNo)).append("\n");
    sb.append("    acquiredFrom: ").append(toIndentedString(acquiredFrom)).append("\n");
    sb.append("    warrantyAvailable: ").append(toIndentedString(warrantyAvailable)).append("\n");
    sb.append("    warrantyExpiryDate: ").append(toIndentedString(warrantyExpiryDate)).append("\n");
    sb.append("    defectLiabilityPeriod: ").append(toIndentedString(defectLiabilityPeriod)).append("\n");
    sb.append("    securityDepositRetained: ").append(toIndentedString(securityDepositRetained)).append("\n");
    sb.append("    securityDepositRealized: ").append(toIndentedString(securityDepositRealized)).append("\n");
    sb.append("    acquisitionDate: ").append(toIndentedString(acquisitionDate)).append("\n");
    sb.append("    originalValue: ").append(toIndentedString(originalValue)).append("\n");
    sb.append("    assetAccount: ").append(toIndentedString(assetAccount)).append("\n");
    sb.append("    accumulatedDepreciationAccount: ").append(toIndentedString(accumulatedDepreciationAccount)).append("\n");
    sb.append("    revaluationReserveAccount: ").append(toIndentedString(revaluationReserveAccount)).append("\n");
    sb.append("    depreciationExpenseAccount: ").append(toIndentedString(depreciationExpenseAccount)).append("\n");
    sb.append("    titleDocumentsAvalable: ").append(toIndentedString(titleDocumentsAvalable)).append("\n");
    sb.append("    usage: ").append(toIndentedString(usage)).append("\n");
    sb.append("    locationDetails: ").append(toIndentedString(locationDetails)).append("\n");
    sb.append("    length: ").append(toIndentedString(length)).append("\n");
    sb.append("    width: ").append(toIndentedString(width)).append("\n");
    sb.append("    height: ").append(toIndentedString(height)).append("\n");
    sb.append("    totalArea: ").append(toIndentedString(totalArea)).append("\n");
    sb.append("    plinthArea: ").append(toIndentedString(plinthArea)).append("\n");
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    longitude: ").append(toIndentedString(longitude)).append("\n");
    sb.append("    latitude: ").append(toIndentedString(latitude)).append("\n");
    sb.append("    floors: ").append(toIndentedString(floors)).append("\n");
    sb.append("    landSurveyNo: ").append(toIndentedString(landSurveyNo)).append("\n");
    sb.append("    cubicContents: ").append(toIndentedString(cubicContents)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    oldCode: ").append(toIndentedString(oldCode)).append("\n");
    sb.append("    departmentCode: ").append(toIndentedString(departmentCode)).append("\n");
    sb.append("    assetCategory: ").append(toIndentedString(assetCategory)).append("\n");
    sb.append("    modeOfAcquisition: ").append(toIndentedString(modeOfAcquisition)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    grossValue: ").append(toIndentedString(grossValue)).append("\n");
    sb.append("    accumulatedDepreciation: ").append(toIndentedString(accumulatedDepreciation)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    dateOfCreation: ").append(toIndentedString(dateOfCreation)).append("\n");
    sb.append("    remarks: ").append(toIndentedString(remarks)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
    sb.append("    assetReference: ").append(toIndentedString(assetReference)).append("\n");
    sb.append("    enableYearWiseDepreciation: ").append(toIndentedString(enableYearWiseDepreciation)).append("\n");
    sb.append("    assetAttributes: ").append(toIndentedString(assetAttributes)).append("\n");
    sb.append("    depreciationRate: ").append(toIndentedString(depreciationRate)).append("\n");
    sb.append("    yearWiseDepreciationRate: ").append(toIndentedString(yearWiseDepreciationRate)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

