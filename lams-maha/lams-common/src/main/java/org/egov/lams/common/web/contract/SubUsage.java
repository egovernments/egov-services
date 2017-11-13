package org.egov.lams.common.web.contract;

import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * An object which holds the  sub usage Type Master info
 */
@ApiModel(description = "An object which holds the  sub usage Type Master info")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-09T07:54:53.690Z")

public class SubUsage   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("usage")
  private Usage usage = null;

  @JsonProperty("code")
  private String code = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("isActive")
  private Boolean isActive = null;

  public SubUsage id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the SubUsage.
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the SubUsage.")

 @Size(min=1,max=256)
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public SubUsage tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * tenant id of the SubUsage
   * @return tenantId
  **/
  @ApiModelProperty(required = true, value = "tenant id of the SubUsage")
  @NotNull

 @Size(min=2,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public SubUsage usage(Usage usage) {
    this.usage = usage;
    return this;
  }

   /**
   * Get usage
   * @return usage
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public Usage getUsage() {
    return usage;
  }

  public void setUsage(Usage usage) {
    this.usage = usage;
  }

  public SubUsage code(String code) {
    this.code = code;
    return this;
  }

   /**
   * code of the SubUsage
   * @return code
  **/
  @ApiModelProperty(required = true, value = "code of the SubUsage")
  @NotNull

 @Size(min=1,max=64)
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public SubUsage name(String name) {
    this.name = name;
    return this;
  }

   /**
   * name of SubUsage
   * @return name
  **/
  @ApiModelProperty(required = true, value = "name of SubUsage")
  @NotNull

 @Size(min=1,max=128)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public SubUsage isActive(Boolean isActive) {
    this.isActive = isActive;
    return this;
  }

   /**
   * is SubUsage active.
   * @return isActive
  **/
  @ApiModelProperty(required = true, value = "is SubUsage active.")
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
    SubUsage subUsage = (SubUsage) o;
    return Objects.equals(this.id, subUsage.id) &&
        Objects.equals(this.tenantId, subUsage.tenantId) &&
        Objects.equals(this.usage, subUsage.usage) &&
        Objects.equals(this.code, subUsage.code) &&
        Objects.equals(this.name, subUsage.name) &&
        Objects.equals(this.isActive, subUsage.isActive);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, usage, code, name, isActive);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SubUsage {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    usage: ").append(toIndentedString(usage)).append("\n");
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

