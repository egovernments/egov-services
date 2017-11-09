package org.egov.inv.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.egov.inv.model.MaterialStoreMapping;
import org.egov.inv.model.Page;
import org.egov.inv.model.ResponseInfo;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Contract class for web response. Array of Material Store Mapping items  are used in case of search ,create or update request.
 */
@ApiModel(description = "Contract class for web response. Array of Material Store Mapping items  are used in case of search ,create or update request.")
@javax.annotation.Generated(value = "org.egov.inv.codegen.languages.SpringCodegen", date = "2017-11-08T13:51:07.770Z")

public class MaterialStoreMappingResponse   {
  @JsonProperty("ResponseInfo")
  private ResponseInfo responseInfo = null;

  @JsonProperty("materialStoreMappings")
  private List<MaterialStoreMapping> materialStoreMappings = null;

  @JsonProperty("page")
  private Page page = null;

  public MaterialStoreMappingResponse responseInfo(ResponseInfo responseInfo) {
    this.responseInfo = responseInfo;
    return this;
  }

   /**
   * Get responseInfo
   * @return responseInfo
  **/
  @ApiModelProperty(value = "")

  @Valid

  public ResponseInfo getResponseInfo() {
    return responseInfo;
  }

  public void setResponseInfo(ResponseInfo responseInfo) {
    this.responseInfo = responseInfo;
  }

  public MaterialStoreMappingResponse materialStoreMappings(List<MaterialStoreMapping> materialStoreMappings) {
    this.materialStoreMappings = materialStoreMappings;
    return this;
  }

  public MaterialStoreMappingResponse addMaterialStoreMappingsItem(MaterialStoreMapping materialStoreMappingsItem) {
    if (this.materialStoreMappings == null) {
      this.materialStoreMappings = new ArrayList<MaterialStoreMapping>();
    }
    this.materialStoreMappings.add(materialStoreMappingsItem);
    return this;
  }

   /**
   * Used for search result and create only
   * @return materialStoreMappings
  **/
  @ApiModelProperty(value = "Used for search result and create only")

  @Valid

  public List<MaterialStoreMapping> getMaterialStoreMappings() {
    return materialStoreMappings;
  }

  public void setMaterialStoreMappings(List<MaterialStoreMapping> materialStoreMappings) {
    this.materialStoreMappings = materialStoreMappings;
  }

  public MaterialStoreMappingResponse page(Page page) {
    this.page = page;
    return this;
  }

   /**
   * Get page
   * @return page
  **/
  @ApiModelProperty(value = "")

  @Valid

  public Page getPage() {
    return page;
  }

  public void setPage(Page page) {
    this.page = page;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MaterialStoreMappingResponse materialStoreMappingResponse = (MaterialStoreMappingResponse) o;
    return Objects.equals(this.responseInfo, materialStoreMappingResponse.responseInfo) &&
        Objects.equals(this.materialStoreMappings, materialStoreMappingResponse.materialStoreMappings) &&
        Objects.equals(this.page, materialStoreMappingResponse.page);
  }

  @Override
  public int hashCode() {
    return Objects.hash(responseInfo, materialStoreMappings, page);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MaterialStoreMappingResponse {\n");
    
    sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
    sb.append("    materialStoreMappings: ").append(toIndentedString(materialStoreMappings)).append("\n");
    sb.append("    page: ").append(toIndentedString(page)).append("\n");
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

