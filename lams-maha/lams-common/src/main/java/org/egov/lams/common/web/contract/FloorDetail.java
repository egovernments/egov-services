package org.egov.lams.common.web.contract;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * A Object holds the data for a floor details of Estate
 */
@ApiModel(description = "A Object holds the data for a floor details of Estate")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-09T07:10:49.937Z")

public class FloorDetail   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("estateRegister")
  private EstateRegister estateRegister = null;

  @JsonProperty("floorNo")
  private String floorNo = null;

  @JsonProperty("floorArea")
  private Double floorArea = null;
  
@JsonProperty("noOfUnits")
  private BigDecimal noOfUnits = null;

  @JsonProperty("units")
  private List<UnitDetail> units = new ArrayList<UnitDetail>();

  public FloorDetail id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the FloorDetail.
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the FloorDetail.")

 @Size(min=1,max=256)
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public FloorDetail tenantId(String tenantId) {
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

  public FloorDetail estateRegister(EstateRegister estateRegister) {
    this.estateRegister = estateRegister;
    return this;
  }

   /**
   * Get estateRegister
   * @return estateRegister
  **/
  @ApiModelProperty(value = "")

  @Valid

  public EstateRegister getEstateRegister() {
    return estateRegister;
  }

  public void setEstateRegister(EstateRegister estateRegister) {
    this.estateRegister = estateRegister;
  }

  public FloorDetail floorNo(String floorNo) {
    this.floorNo = floorNo;
    return this;
  }

   /**
   * Floor no
   * @return floorNo
  **/
  @ApiModelProperty(required = true, value = "Floor no")
  @NotNull

 @Size(min=1,max=64)
  public String getFloorNo() {
    return floorNo;
  }

  public void setFloorNo(String floorNo) {
    this.floorNo = floorNo;
  }

  public FloorDetail floorArea(Double floorArea) {
    this.floorArea = floorArea;
    return this;
  }

   /**
   * Area of a floor
   * @return floorArea
  **/
  @ApiModelProperty(required = true, value = "Area of a floor")
  @NotNull


  public Double getFloorArea() {
    return floorArea;
  }

  public void setFloorArea(Double floorArea) {
    this.floorArea = floorArea;
  }

  public FloorDetail noOfUnits(BigDecimal noOfUnits) {
    this.noOfUnits = noOfUnits;
    return this;
  }

   /**
   * No.of units in a floor
   * @return noOfUnits
  **/
  @ApiModelProperty(required = true, value = "No.of units in a floor")
  @NotNull

  @Valid

  public BigDecimal getNoOfUnits() {
    return noOfUnits;
  }

  public void setNoOfUnits(BigDecimal noOfUnits) {
    this.noOfUnits = noOfUnits;
  }

  public FloorDetail units(List<UnitDetail> units) {
    this.units = units;
    return this;
  }

  public FloorDetail addUnitsItem(UnitDetail unitsItem) {
    this.units.add(unitsItem);
    return this;
  }

   /**
   * Get units
   * @return units
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public List<UnitDetail> getUnits() {
    return units;
  }

  public void setUnits(List<UnitDetail> units) {
    this.units = units;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FloorDetail floorDetail = (FloorDetail) o;
    return Objects.equals(this.id, floorDetail.id) &&
        Objects.equals(this.tenantId, floorDetail.tenantId) &&
        Objects.equals(this.estateRegister, floorDetail.estateRegister) &&
        Objects.equals(this.floorNo, floorDetail.floorNo) &&
        Objects.equals(this.floorArea, floorDetail.floorArea) &&
        Objects.equals(this.noOfUnits, floorDetail.noOfUnits) &&
        Objects.equals(this.units, floorDetail.units);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, estateRegister, floorNo, floorArea, noOfUnits, units);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FloorDetail {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    estateRegister: ").append(toIndentedString(estateRegister)).append("\n");
    sb.append("    floorNo: ").append(toIndentedString(floorNo)).append("\n");
    sb.append("    floorArea: ").append(toIndentedString(floorArea)).append("\n");
    sb.append("    noOfUnits: ").append(toIndentedString(noOfUnits)).append("\n");
    sb.append("    units: ").append(toIndentedString(units)).append("\n");
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

