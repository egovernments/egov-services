package org.egov.works.estimate.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * An Object that holds Present condition of the Land asset
 */
@ApiModel(description = "An Object that holds Present condition of the Land asset")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-29T09:03:53.949Z")

public class LandAssetPresentCondition   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("name")
  private String name = null;

  public LandAssetPresentCondition id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Present condition of the Land asset
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Present condition of the Land asset")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public LandAssetPresentCondition tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the Present condition of the Land asset
   * @return tenantId
  **/
  @ApiModelProperty(required = true, value = "Tenant id of the Present condition of the Land asset")
  @NotNull

 @Size(min=2,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public LandAssetPresentCondition name(String name) {
    this.name = name;
    return this;
  }

   /**
   * Present condition of the Land asset
   * @return name
  **/
  @ApiModelProperty(required = true, value = "Present condition of the Land asset")
  @NotNull

 @Pattern(regexp="[a-zA-Z0-9\\s\\.,]+") @Size(min=1,max=256)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LandAssetPresentCondition landAssetPresentCondition = (LandAssetPresentCondition) o;
    return Objects.equals(this.id, landAssetPresentCondition.id) &&
        Objects.equals(this.tenantId, landAssetPresentCondition.tenantId) &&
        Objects.equals(this.name, landAssetPresentCondition.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, name);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LandAssetPresentCondition {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
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

