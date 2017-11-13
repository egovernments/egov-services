package io.swagger.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.egov.common.contract.response.ResponseInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;

/**
 * Contract class for web response. Array of Material items  are used in case of search ,create or update request.
 */
@ApiModel(description = "Contract class for web response. Array of Material items  are used in case of search ,create or update request.")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-08T06:17:26.594Z")
@Builder
public class MaterialResponse   {
  @JsonProperty("ResponseInfo")
  private org.egov.common.contract.response.ResponseInfo responseInfo = null;

  @JsonProperty("materials")
  private List<Material> materials = null;

  @JsonProperty("page")
  private Page page = null;

  public MaterialResponse responseInfo(org.egov.common.contract.response.ResponseInfo responseInfo) {
    this.responseInfo = responseInfo;
    return this;
  }

   /**
   * Get responseInfo
   * @return responseInfo
  **/
  @ApiModelProperty(value = "")

  @Valid

  public org.egov.common.contract.response.ResponseInfo getResponseInfo() {
    return responseInfo;
  }

  public void setResponseInfo(ResponseInfo responseInfo) {
    this.responseInfo = responseInfo;
  }

  public MaterialResponse materials(List<Material> materials) {
    this.materials = materials;
    return this;
  }

  public MaterialResponse addMaterialsItem(Material materialsItem) {
    if (this.materials == null) {
      this.materials = new ArrayList<Material>();
    }
    this.materials.add(materialsItem);
    return this;
  }

   /**
   * Used for search result and create only
   * @return materials
  **/
  @ApiModelProperty(value = "Used for search result and create only")

  @Valid

  public List<Material> getMaterials() {
    return materials;
  }

  public void setMaterials(List<Material> materials) {
    this.materials = materials;
  }

  public MaterialResponse page(Page page) {
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
    MaterialResponse materialResponse = (MaterialResponse) o;
    return Objects.equals(this.responseInfo, materialResponse.responseInfo) &&
        Objects.equals(this.materials, materialResponse.materials) &&
        Objects.equals(this.page, materialResponse.page);
  }

  @Override
  public int hashCode() {
    return Objects.hash(responseInfo, materials, page);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MaterialResponse {\n");
    
    sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
    sb.append("    materials: ").append(toIndentedString(materials)).append("\n");
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

