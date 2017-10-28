package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.Material;
import io.swagger.model.RequestInfo;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Contract class for web request. Array of Material items  are used in case of create or update
 */
@ApiModel(description = "Contract class for web request. Array of Material items  are used in case of create or update")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-28T13:21:55.964+05:30")

public class MaterialRequest   {
  @JsonProperty("requestInfo")
  private RequestInfo requestInfo = null;

  @JsonProperty("materials")
  @Valid
  private List<Material> materials = null;

  public MaterialRequest requestInfo(RequestInfo requestInfo) {
    this.requestInfo = requestInfo;
    return this;
  }

   /**
   * Get requestInfo
   * @return requestInfo
  **/
  @ApiModelProperty(value = "")

  @Valid

  public RequestInfo getRequestInfo() {
    return requestInfo;
  }

  public void setRequestInfo(RequestInfo requestInfo) {
    this.requestInfo = requestInfo;
  }

  public MaterialRequest materials(List<Material> materials) {
    this.materials = materials;
    return this;
  }

  public MaterialRequest addMaterialsItem(Material materialsItem) {
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MaterialRequest materialRequest = (MaterialRequest) o;
    return Objects.equals(this.requestInfo, materialRequest.requestInfo) &&
        Objects.equals(this.materials, materialRequest.materials);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestInfo, materials);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MaterialRequest {\n");
    
    sb.append("    requestInfo: ").append(toIndentedString(requestInfo)).append("\n");
    sb.append("    materials: ").append(toIndentedString(materials)).append("\n");
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

