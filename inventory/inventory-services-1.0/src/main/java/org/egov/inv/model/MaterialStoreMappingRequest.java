package org.egov.inv.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.egov.inv.model.MaterialStoreMapping;
import org.egov.inv.model.RequestInfo;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Contract class for web request. Array of Material items  are used in case of create or update
 */
@ApiModel(description = "Contract class for web request. Array of Material items  are used in case of create or update")
@javax.annotation.Generated(value = "org.egov.inv.codegen.languages.SpringCodegen", date = "2017-11-08T13:51:07.770Z")

public class MaterialStoreMappingRequest   {
  @JsonProperty("RequestInfo")
  private RequestInfo requestInfo = null;

  @JsonProperty("materialStoreMappings")
  private List<MaterialStoreMapping> materialStoreMappings = new ArrayList<MaterialStoreMapping>();

  public MaterialStoreMappingRequest requestInfo(RequestInfo requestInfo) {
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

  public MaterialStoreMappingRequest materialStoreMappings(List<MaterialStoreMapping> materialStoreMappings) {
    this.materialStoreMappings = materialStoreMappings;
    return this;
  }

  public MaterialStoreMappingRequest addMaterialStoreMappingsItem(MaterialStoreMapping materialStoreMappingsItem) {
    this.materialStoreMappings.add(materialStoreMappingsItem);
    return this;
  }

   /**
   * Used for search result and create only
   * @return materialStoreMappings
  **/
  @ApiModelProperty(required = true, value = "Used for search result and create only")
  @NotNull

  @Valid

  public List<MaterialStoreMapping> getMaterialStoreMappings() {
    return materialStoreMappings;
  }

  public void setMaterialStoreMappings(List<MaterialStoreMapping> materialStoreMappings) {
    this.materialStoreMappings = materialStoreMappings;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MaterialStoreMappingRequest materialStoreMappingRequest = (MaterialStoreMappingRequest) o;
    return Objects.equals(this.requestInfo, materialStoreMappingRequest.requestInfo) &&
        Objects.equals(this.materialStoreMappings, materialStoreMappingRequest.materialStoreMappings);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestInfo, materialStoreMappings);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MaterialStoreMappingRequest {\n");
    
    sb.append("    requestInfo: ").append(toIndentedString(requestInfo)).append("\n");
    sb.append("    materialStoreMappings: ").append(toIndentedString(materialStoreMappings)).append("\n");
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

