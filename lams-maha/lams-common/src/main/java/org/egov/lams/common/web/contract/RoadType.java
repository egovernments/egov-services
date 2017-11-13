package org.egov.lams.common.web.contract;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * An object which holds the  Road Type Master info
 */
@ApiModel(description = "An object which holds the  Road Type Master info")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-09T07:10:49.937Z")

public class RoadType   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("code")
  private String code = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("isActive")
  private Boolean isActive = null;

  public RoadType id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the RoadType.
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the RoadType.")

 @Size(min=1,max=256)
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public RoadType tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * tenant id of the RoadType
   * @return tenantId
  **/
  @ApiModelProperty(required = true, value = "tenant id of the RoadType")
  @NotNull

 @Size(min=2,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public RoadType code(String code) {
    this.code = code;
    return this;
  }

   /**
   * code of the RoadType
   * @return code
  **/
  @ApiModelProperty(required = true, value = "code of the RoadType")
  @NotNull

 @Size(min=1,max=64)
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public RoadType name(String name) {
    this.name = name;
    return this;
  }

   /**
   * name of Mode Of Acquisition Master
   * @return name
  **/
  @ApiModelProperty(required = true, value = "name of Mode Of Acquisition Master")
  @NotNull

 @Size(min=1,max=128)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public RoadType isActive(Boolean isActive) {
    this.isActive = isActive;
    return this;
  }

   /**
   * is RoadType active.
   * @return isActive
  **/
  @ApiModelProperty(required = true, value = "is RoadType active.")
  @NotNull


  public Boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RoadType roadType = (RoadType) o;
    return Objects.equals(this.id, roadType.id) &&
        Objects.equals(this.tenantId, roadType.tenantId) &&
        Objects.equals(this.code, roadType.code) &&
        Objects.equals(this.name, roadType.name) &&
        Objects.equals(this.isActive, roadType.isActive);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, code, name, isActive);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RoadType {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    isActive: ").append(toIndentedString(isActive)).append("\n");
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

