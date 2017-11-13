package org.egov.lams.common.web.contract;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * An object which holds the  planning Type Master info
 */
@ApiModel(description = "An object which holds the  planning Type Master info")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-09T07:10:49.937Z")

public class PlaningType   {
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

  public PlaningType id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the PlanningType.
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the PlanningType.")

 @Size(min=1,max=256)
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public PlaningType tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * tenant id of the PlanningType
   * @return tenantId
  **/
  @ApiModelProperty(required = true, value = "tenant id of the PlanningType")
  @NotNull

 @Size(min=2,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public PlaningType code(String code) {
    this.code = code;
    return this;
  }

   /**
   * code of the PlanningType
   * @return code
  **/
  @ApiModelProperty(required = true, value = "code of the PlanningType")
  @NotNull

 @Size(min=1,max=64)
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public PlaningType name(String name) {
    this.name = name;
    return this;
  }

   /**
   * name of planning Type
   * @return name
  **/
  @ApiModelProperty(required = true, value = "name of planning Type")
  @NotNull

 @Size(min=1,max=128)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public PlaningType isActive(Boolean isActive) {
    this.isActive = isActive;
    return this;
  }

   /**
   * is PlanningType active.
   * @return isActive
  **/
  @ApiModelProperty(required = true, value = "is PlanningType active.")
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
    PlaningType planingType = (PlaningType) o;
    return Objects.equals(this.id, planingType.id) &&
        Objects.equals(this.tenantId, planingType.tenantId) &&
        Objects.equals(this.code, planingType.code) &&
        Objects.equals(this.name, planingType.name) &&
        Objects.equals(this.isActive, planingType.isActive);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, code, name, isActive);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlaningType {\n");
    
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

