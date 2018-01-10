package org.egov.inv.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Contract class for web request. Array of Material Types  are used in case of create or update
 */
@ApiModel(description = "Contract class for web request. Array of Material Types  are used in case of create or update")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-01-10T06:55:29.202Z")

public class MaterialTypeRequest   {
  @JsonProperty("RequestInfo")
  private RequestInfo requestInfo = null;

  @JsonProperty("materialTypes")
  private List<MaterialType> materialTypes = new ArrayList<MaterialType>();

  public MaterialTypeRequest requestInfo(RequestInfo requestInfo) {
    this.requestInfo = requestInfo;
    return this;
  }

   /**
   * Get requestInfo
   * @return requestInfo
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public RequestInfo getRequestInfo() {
    return requestInfo;
  }

  public void setRequestInfo(RequestInfo requestInfo) {
    this.requestInfo = requestInfo;
  }

  public MaterialTypeRequest materialTypes(List<MaterialType> materialTypes) {
    this.materialTypes = materialTypes;
    return this;
  }

  public MaterialTypeRequest addMaterialTypesItem(MaterialType materialTypesItem) {
    this.materialTypes.add(materialTypesItem);
    return this;
  }

   /**
   * Used for search result and create only
   * @return materialTypes
  **/
  @ApiModelProperty(required = true, value = "Used for search result and create only")
  @NotNull

  @Valid

  public List<MaterialType> getMaterialTypes() {
    return materialTypes;
  }

  public void setMaterialTypes(List<MaterialType> materialTypes) {
    this.materialTypes = materialTypes;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MaterialTypeRequest materialTypeRequest = (MaterialTypeRequest) o;
    return Objects.equals(this.requestInfo, materialTypeRequest.requestInfo) &&
        Objects.equals(this.materialTypes, materialTypeRequest.materialTypes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestInfo, materialTypes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MaterialTypeRequest {\n");
    
    sb.append("    requestInfo: ").append(toIndentedString(requestInfo)).append("\n");
    sb.append("    materialTypes: ").append(toIndentedString(materialTypes)).append("\n");
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

