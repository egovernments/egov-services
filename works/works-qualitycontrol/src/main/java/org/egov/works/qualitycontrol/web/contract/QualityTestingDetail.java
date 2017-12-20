package org.egov.works.qualitycontrol.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * An Object that holds the basic data for Quality Testing Detail
 */
@ApiModel(description = "An Object that holds the basic data for Quality Testing Detail")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-12-18T07:23:14.953Z")

public class QualityTestingDetail   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("qualityTesting")
  private String qualityTesting = null;

  @JsonProperty("materialName")
  private String materialName = null;

  @JsonProperty("testName")
  private String testName = null;

  @JsonProperty("resultUnit")
  private String resultUnit = null;

  @JsonProperty("minimumValue")
  private BigDecimal minimumValue = null;

  @JsonProperty("maximumValue")
  private BigDecimal maximumValue = null;

  @JsonProperty("hodRemarks")
  private String hodRemarks = null;

  @JsonProperty("coRemarks")
  private String coRemarks = null;

  @JsonProperty("deleted")
  private Boolean deleted = false;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public QualityTestingDetail id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Quality Testing Detail
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Quality Testing Detail")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public QualityTestingDetail tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the Quality Testing Detail
   * @return tenantId
  **/
  @ApiModelProperty(required = true, value = "Tenant id of the Quality Testing Detail")
  @NotNull

 @Size(min=2,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public QualityTestingDetail qualityTesting(String qualityTesting) {
    this.qualityTesting = qualityTesting;
    return this;
  }

   /**
   * Reference of Quality Testing object
   * @return qualityTesting
  **/
  @ApiModelProperty(required = true, value = "Reference of Quality Testing object")
  //@NotNull


  public String getQualityTesting() {
    return qualityTesting;
  }

  public void setQualityTesting(String qualityTesting) {
    this.qualityTesting = qualityTesting;
  }

  public QualityTestingDetail materialName(String materialName) {
    this.materialName = materialName;
    return this;
  }

   /**
   * Material name for the Quality Testing
   * @return materialName
  **/
  @ApiModelProperty(required = true, value = "Material name for the Quality Testing")
  @NotNull

 @Pattern(regexp="[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]+") @Size(min=1,max=100)
  public String getMaterialName() {
    return materialName;
  }

  public void setMaterialName(String materialName) {
    this.materialName = materialName;
  }

  public QualityTestingDetail testName(String testName) {
    this.testName = testName;
    return this;
  }

   /**
   * Test name for the Quality Testing
   * @return testName
  **/
  @ApiModelProperty(required = true, value = "Test name for the Quality Testing")
  @NotNull

 @Pattern(regexp="[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]+") @Size(min=1,max=100)
  public String getTestName() {
    return testName;
  }

  public void setTestName(String testName) {
    this.testName = testName;
  }

  public QualityTestingDetail resultUnit(String resultUnit) {
    this.resultUnit = resultUnit;
    return this;
  }

   /**
   * Result unit for the Quality Testing
   * @return resultUnit
  **/
  @ApiModelProperty(required = true, value = "Result unit for the Quality Testing")
  @NotNull

 @Pattern(regexp="[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]+") @Size(min=1,max=100)
  public String getResultUnit() {
    return resultUnit;
  }

  public void setResultUnit(String resultUnit) {
    this.resultUnit = resultUnit;
  }

  public QualityTestingDetail minimumValue(BigDecimal minimumValue) {
    this.minimumValue = minimumValue;
    return this;
  }

   /**
   * Minimum Value for the Quality Testing
   * @return minimumValue
  **/
  @ApiModelProperty(required = true, value = "Minimum Value for the Quality Testing")
  @NotNull

  @Valid

  public BigDecimal getMinimumValue() {
    return minimumValue;
  }

  public void setMinimumValue(BigDecimal minimumValue) {
    this.minimumValue = minimumValue;
  }

  public QualityTestingDetail maximumValue(BigDecimal maximumValue) {
    this.maximumValue = maximumValue;
    return this;
  }

   /**
   * Maximum value for the Quality Testing
   * @return maximumValue
  **/
  @ApiModelProperty(required = true, value = "Maximum value for the Quality Testing")
  @NotNull

  @Valid

  public BigDecimal getMaximumValue() {
    return maximumValue;
  }

  public void setMaximumValue(BigDecimal maximumValue) {
    this.maximumValue = maximumValue;
  }

  public QualityTestingDetail hodRemarks(String hodRemarks) {
    this.hodRemarks = hodRemarks;
    return this;
  }

   /**
   * HOD/Eng remarks for the Quality Testing
   * @return hodRemarks
  **/
  @ApiModelProperty(required = true, value = "HOD/Eng remarks for the Quality Testing")
  @NotNull

 @Pattern(regexp="[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]+") @Size(min=1,max=1024)
  public String getHodRemarks() {
    return hodRemarks;
  }

  public void setHodRemarks(String hodRemarks) {
    this.hodRemarks = hodRemarks;
  }

  public QualityTestingDetail coRemarks(String coRemarks) {
    this.coRemarks = coRemarks;
    return this;
  }

   /**
   * CO remarks for the Quality Testing
   * @return coRemarks
  **/
  @ApiModelProperty(required = true, value = "CO remarks for the Quality Testing")
  @NotNull

 @Pattern(regexp="[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]+") @Size(min=1,max=1024)
  public String getCoRemarks() {
    return coRemarks;
  }

  public void setCoRemarks(String coRemarks) {
    this.coRemarks = coRemarks;
  }

  public QualityTestingDetail deleted(Boolean deleted) {
    this.deleted = deleted;
    return this;
  }

   /**
   * Boolean value to identify whether the object is deleted or not from UI.
   * @return deleted
  **/
  @ApiModelProperty(value = "Boolean value to identify whether the object is deleted or not from UI.")


  public Boolean getDeleted() {
    return deleted;
  }

  public void setDeleted(Boolean deleted) {
    this.deleted = deleted;
  }

  public QualityTestingDetail auditDetails(AuditDetails auditDetails) {
    this.auditDetails = auditDetails;
    return this;
  }

   /**
   * Get auditDetails
   * @return auditDetails
  **/
  @ApiModelProperty(value = "")

  @Valid

  public AuditDetails getAuditDetails() {
    return auditDetails;
  }

  public void setAuditDetails(AuditDetails auditDetails) {
    this.auditDetails = auditDetails;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QualityTestingDetail qualityTestingDetail = (QualityTestingDetail) o;
    return Objects.equals(this.id, qualityTestingDetail.id) &&
        Objects.equals(this.tenantId, qualityTestingDetail.tenantId) &&
        Objects.equals(this.qualityTesting, qualityTestingDetail.qualityTesting) &&
        Objects.equals(this.materialName, qualityTestingDetail.materialName) &&
        Objects.equals(this.testName, qualityTestingDetail.testName) &&
        Objects.equals(this.resultUnit, qualityTestingDetail.resultUnit) &&
        Objects.equals(this.minimumValue, qualityTestingDetail.minimumValue) &&
        Objects.equals(this.maximumValue, qualityTestingDetail.maximumValue) &&
        Objects.equals(this.hodRemarks, qualityTestingDetail.hodRemarks) &&
        Objects.equals(this.coRemarks, qualityTestingDetail.coRemarks) &&
        Objects.equals(this.deleted, qualityTestingDetail.deleted) &&
        Objects.equals(this.auditDetails, qualityTestingDetail.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, qualityTesting, materialName, testName, resultUnit, minimumValue, maximumValue, hodRemarks, coRemarks, deleted, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QualityTestingDetail {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    qualityTesting: ").append(toIndentedString(qualityTesting)).append("\n");
    sb.append("    materialName: ").append(toIndentedString(materialName)).append("\n");
    sb.append("    testName: ").append(toIndentedString(testName)).append("\n");
    sb.append("    resultUnit: ").append(toIndentedString(resultUnit)).append("\n");
    sb.append("    minimumValue: ").append(toIndentedString(minimumValue)).append("\n");
    sb.append("    maximumValue: ").append(toIndentedString(maximumValue)).append("\n");
    sb.append("    hodRemarks: ").append(toIndentedString(hodRemarks)).append("\n");
    sb.append("    coRemarks: ").append(toIndentedString(coRemarks)).append("\n");
    sb.append("    deleted: ").append(toIndentedString(deleted)).append("\n");
    sb.append("    auditDetails: ").append(toIndentedString(auditDetails)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

