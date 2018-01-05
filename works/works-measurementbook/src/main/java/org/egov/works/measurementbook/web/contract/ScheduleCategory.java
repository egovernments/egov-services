package org.egov.works.measurementbook.web.contract;

import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * An Object which holds Schedule Category Master Data
 */
@ApiModel(description = "An Object which holds Schedule Category Master Data")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-12-20T10:00:39.005Z")

public class ScheduleCategory   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("code")
  private String code = null;

  public ScheduleCategory id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Schedule Category
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Schedule Category")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public ScheduleCategory tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the Schedule Category
   * @return tenantId
  **/
  @ApiModelProperty(required = true, value = "Tenant id of the Schedule Category")
  @NotNull

 @Size(min=2,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public ScheduleCategory name(String name) {
    this.name = name;
    return this;
  }

   /**
   * Unique name of the Schedule Category
   * @return name
  **/
  @ApiModelProperty(required = true, value = "Unique name of the Schedule Category")
  @NotNull

 @Pattern(regexp="[a-zA-Z0-9\\s\\.,]+") @Size(min=1,max=100)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ScheduleCategory code(String code) {
    this.code = code;
    return this;
  }

   /**
   * Unique code of the Schedule Category
   * @return code
  **/
  @ApiModelProperty(required = true, value = "Unique code of the Schedule Category")
  @NotNull

 @Pattern(regexp="[a-zA-Z0-9-\\\\]+") @Size(min=1,max=100)
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ScheduleCategory scheduleCategory = (ScheduleCategory) o;
    return Objects.equals(this.id, scheduleCategory.id) &&
        Objects.equals(this.tenantId, scheduleCategory.tenantId) &&
        Objects.equals(this.name, scheduleCategory.name) &&
        Objects.equals(this.code, scheduleCategory.code);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, name, code);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ScheduleCategory {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
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

