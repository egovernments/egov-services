package org.egov.lams.common.web.contract;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * An object which holds the Mode Of Acquisition Master info
 */
@ApiModel(description = "An object which holds the Mode Of Acquisition Master info")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-09T07:10:49.937Z")

public class ModeOfAcquisition   {
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

  public ModeOfAcquisition id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the ModeOfAcquisition.
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the ModeOfAcquisition.")

 @Size(min=1,max=256)
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public ModeOfAcquisition tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * tenant id of the ModeOfAcquisition
   * @return tenantId
  **/
  @ApiModelProperty(required = true, value = "tenant id of the ModeOfAcquisition")
  @NotNull

 @Size(min=2,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public ModeOfAcquisition code(String code) {
    this.code = code;
    return this;
  }

   /**
   * code of the ModeOfAcquisition
   * @return code
  **/
  @ApiModelProperty(required = true, value = "code of the ModeOfAcquisition")
  @NotNull

 @Size(min=1,max=64)
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public ModeOfAcquisition name(String name) {
    this.name = name;
    return this;
  }

   /**
   * Mode Of Acquisition Master
   * @return name
  **/
  @ApiModelProperty(required = true, value = "Mode Of Acquisition Master")
  @NotNull

 @Size(min=1,max=128)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ModeOfAcquisition isActive(Boolean isActive) {
    this.isActive = isActive;
    return this;
  }

   /**
   * is ModeOfAcquisition is active.
   * @return isActive
  **/
  @ApiModelProperty(required = true, value = "is ModeOfAcquisition is active.")
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
    ModeOfAcquisition modeOfAcquisition = (ModeOfAcquisition) o;
    return Objects.equals(this.id, modeOfAcquisition.id) &&
        Objects.equals(this.tenantId, modeOfAcquisition.tenantId) &&
        Objects.equals(this.code, modeOfAcquisition.code) &&
        Objects.equals(this.name, modeOfAcquisition.name) &&
        Objects.equals(this.isActive, modeOfAcquisition.isActive);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, code, name, isActive);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ModeOfAcquisition {\n");
    
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

