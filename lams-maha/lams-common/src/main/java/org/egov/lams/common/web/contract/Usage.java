package org.egov.lams.common.web.contract;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * An object which holds the  usage Type Master info
 */
@ApiModel(description = "An object which holds the  usage Type Master info")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-09T07:54:53.690Z")

public class Usage   {
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

  public Usage id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Usage.
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Usage.")

 @Size(min=1,max=256)
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Usage tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * tenant id of the Usage
   * @return tenantId
  **/
  @ApiModelProperty(required = true, value = "tenant id of the Usage")
  @NotNull

 @Size(min=2,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public Usage code(String code) {
    this.code = code;
    return this;
  }

   /**
   * code of the Usage
   * @return code
  **/
  @ApiModelProperty(required = true, value = "code of the Usage")
  @NotNull

 @Size(min=1,max=64)
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Usage name(String name) {
    this.name = name;
    return this;
  }

   /**
   * name of Usage
   * @return name
  **/
  @ApiModelProperty(required = true, value = "name of Usage")
  @NotNull

 @Size(min=1,max=128)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Usage isActive(Boolean isActive) {
    this.isActive = isActive;
    return this;
  }

   /**
   * is Usage active.
   * @return isActive
  **/
  @ApiModelProperty(required = true, value = "is Usage active.")
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
    Usage usage = (Usage) o;
    return Objects.equals(this.id, usage.id) &&
        Objects.equals(this.tenantId, usage.tenantId) &&
        Objects.equals(this.code, usage.code) &&
        Objects.equals(this.name, usage.name) &&
        Objects.equals(this.isActive, usage.isActive);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, code, name, isActive);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Usage {\n");
    
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

