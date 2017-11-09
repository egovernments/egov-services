package org.egov.inv.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * modeOfAcquisition defined master type ModeOfAcquisiton are shown in the drop down.
 */
@ApiModel(description = "modeOfAcquisition defined master type ModeOfAcquisiton are shown in the drop down.")
@javax.annotation.Generated(value = "org.egov.inv.codegen.languages.SpringCodegen", date = "2017-11-08T13:51:07.770Z")

public class ModeOfAcquisition   {
  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("code")
  private String code = null;

  @JsonProperty("isActive")
  private Boolean isActive = null;

  public ModeOfAcquisition id(Long id) {
    this.id = id;
    return this;
  }

   /**
   * id fo the object
   * @return id
  **/
  @ApiModelProperty(value = "id fo the object")


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ModeOfAcquisition tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * tenantid of the modeOfAcquisition
   * @return tenantId
  **/
  @ApiModelProperty(value = "tenantid of the modeOfAcquisition")


  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public ModeOfAcquisition name(String name) {
    this.name = name;
    return this;
  }

   /**
   * name of the modeOfAcquisition
   * @return name
  **/
  @ApiModelProperty(value = "name of the modeOfAcquisition")


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ModeOfAcquisition code(String code) {
    this.code = code;
    return this;
  }

   /**
   * code of the modeOfAcquisition
   * @return code
  **/
  @ApiModelProperty(value = "code of the modeOfAcquisition")


  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public ModeOfAcquisition isActive(Boolean isActive) {
    this.isActive = isActive;
    return this;
  }

   /**
   * states whether the value is active or not.
   * @return isActive
  **/
  @ApiModelProperty(value = "states whether the value is active or not.")


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
        Objects.equals(this.name, modeOfAcquisition.name) &&
        Objects.equals(this.code, modeOfAcquisition.code) &&
        Objects.equals(this.isActive, modeOfAcquisition.isActive);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, name, code, isActive);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ModeOfAcquisition {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
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

