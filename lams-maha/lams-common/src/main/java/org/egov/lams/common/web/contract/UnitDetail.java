package org.egov.lams.common.web.contract;

import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * A Object holds the data for a unit details of floor
 */
@ApiModel(description = "A Object holds the data for a unit details of floor")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-09T07:10:49.937Z")

public class UnitDetail   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("floor")
  private FloorDetail floor = null;

  @JsonProperty("usage")
  private String usage = null;

  @JsonProperty("previousUnitNo")
  private String previousUnitNo = null;

  @JsonProperty("builtUpArea")
  private Double builtUpArea = null;

  @JsonProperty("holdingType")
  private String holdingType = null;

  @JsonProperty("departmentName")
  private String departmentName = null;

  @JsonProperty("description")
  private String description = null;

  public UnitDetail id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the UnitDetail.
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the UnitDetail.")

 @Size(min=1,max=256)
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public UnitDetail tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Unique Identifier of the tenant
   * @return tenantId
  **/
  @ApiModelProperty(readOnly = true, value = "Unique Identifier of the tenant")

 @Size(min=2,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public UnitDetail floor(FloorDetail floor) {
    this.floor = floor;
    return this;
  }

   /**
   * Get floor
   * @return floor
  **/

  public FloorDetail getFloor() {
    return floor;
  }

  public void setFloor(FloorDetail floor) {
    this.floor = floor;
  }

  public UnitDetail usage(String usage) {
    this.usage = usage;
    return this;
  }

   /**
   * Usage type of a unit
   * @return usage
  **/
  @ApiModelProperty(required = true, value = "Usage type of a unit")
  @NotNull

 @Size(min=1,max=64)
  public String getUsage() {
    return usage;
  }

  public void setUsage(String usage) {
    this.usage = usage;
  }

  public UnitDetail previousUnitNo(String previousUnitNo) {
    this.previousUnitNo = previousUnitNo;
    return this;
  }

   /**
   * Previous unitno of a unit
   * @return previousUnitNo
  **/
  @ApiModelProperty(value = "Previous unitno of a unit")

 @Size(min=1,max=64)
  public String getPreviousUnitNo() {
    return previousUnitNo;
  }

  public void setPreviousUnitNo(String previousUnitNo) {
    this.previousUnitNo = previousUnitNo;
  }

  public UnitDetail builtUpArea(Double builtUpArea) {
    this.builtUpArea = builtUpArea;
    return this;
  }

   /**
   * Built up area of a unit
   * @return builtUpArea
  **/
  @ApiModelProperty(required = true, value = "Built up area of a unit")
  @NotNull


  public Double getBuiltUpArea() {
    return builtUpArea;
  }

  public void setBuiltUpArea(Double builtUpArea) {
    this.builtUpArea = builtUpArea;
  }

  public UnitDetail holdingType(String holdingType) {
    this.holdingType = holdingType;
    return this;
  }

   /**
   * holding type of a unit.
   * @return holdingType
  **/
  @ApiModelProperty(required = true, value = "holding type of a unit.")
  @NotNull

 @Size(min=1,max=64)
  public String getHoldingType() {
    return holdingType;
  }

  public void setHoldingType(String holdingType) {
    this.holdingType = holdingType;
  }

  public UnitDetail departmentName(String departmentName) {
    this.departmentName = departmentName;
    return this;
  }

   /**
   * Department name in a unit.
   * @return departmentName
  **/
  @ApiModelProperty(value = "Department name in a unit.")

 @Size(min=1,max=64)
  public String getDepartmentName() {
    return departmentName;
  }

  public void setDepartmentName(String departmentName) {
    this.departmentName = departmentName;
  }

  public UnitDetail description(String description) {
    this.description = description;
    return this;
  }

   /**
   * description of a unit.
   * @return description
  **/
  @ApiModelProperty(value = "description of a unit.")

 @Size(min=1,max=512)
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UnitDetail unitDetail = (UnitDetail) o;
    return Objects.equals(this.id, unitDetail.id) &&
        Objects.equals(this.tenantId, unitDetail.tenantId) &&
        Objects.equals(this.floor, unitDetail.floor) &&
        Objects.equals(this.usage, unitDetail.usage) &&
        Objects.equals(this.previousUnitNo, unitDetail.previousUnitNo) &&
        Objects.equals(this.builtUpArea, unitDetail.builtUpArea) &&
        Objects.equals(this.holdingType, unitDetail.holdingType) &&
        Objects.equals(this.departmentName, unitDetail.departmentName) &&
        Objects.equals(this.description, unitDetail.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, floor, usage, previousUnitNo, builtUpArea, holdingType, departmentName, description);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UnitDetail {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    floor: ").append(toIndentedString(floor)).append("\n");
    sb.append("    usage: ").append(toIndentedString(usage)).append("\n");
    sb.append("    previousUnitNo: ").append(toIndentedString(previousUnitNo)).append("\n");
    sb.append("    builtUpArea: ").append(toIndentedString(builtUpArea)).append("\n");
    sb.append("    holdingType: ").append(toIndentedString(holdingType)).append("\n");
    sb.append("    departmentName: ").append(toIndentedString(departmentName)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
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

