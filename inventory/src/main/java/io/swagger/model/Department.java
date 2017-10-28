package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.AuditDetails;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * 
 */
@ApiModel(description = "")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-28T13:21:55.964+05:30")

public class Department   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("code")
  private String code = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("active")
  private Boolean active = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public Department id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Department 
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Department ")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Department tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the Department
   * @return tenantId
  **/
  @ApiModelProperty(value = "Tenant id of the Department")

 @Size(min=4,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public Department code(String code) {
    this.code = code;
    return this;
  }

   /**
   * code of the Department 
   * @return code
  **/
  @ApiModelProperty(value = "code of the Department ")


  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Department name(String name) {
    this.name = name;
    return this;
  }

   /**
   * name of the Department 
   * @return name
  **/
  @ApiModelProperty(value = "name of the Department ")


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Department active(Boolean active) {
    this.active = active;
    return this;
  }

   /**
   * Whether Department is Active or not. If the value is TRUE, then Department is active,If the value is FALSE then Department is inactive,Default value is TRUE 
   * @return active
  **/
  @ApiModelProperty(value = "Whether Department is Active or not. If the value is TRUE, then Department is active,If the value is FALSE then Department is inactive,Default value is TRUE ")


  public Boolean isActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public Department auditDetails(AuditDetails auditDetails) {
    this.auditDetails = auditDetails;
    return this;
  }

   /**
   * Get auditDetails
   * @return auditDetails
  **/
  @ApiModelProperty(value = "")

  @Valid

  public AuditDetails getAuditDetails() {
    return auditDetails;
  }

  public void setAuditDetails(AuditDetails auditDetails) {
    this.auditDetails = auditDetails;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Department department = (Department) o;
    return Objects.equals(this.id, department.id) &&
        Objects.equals(this.tenantId, department.tenantId) &&
        Objects.equals(this.code, department.code) &&
        Objects.equals(this.name, department.name) &&
        Objects.equals(this.active, department.active) &&
        Objects.equals(this.auditDetails, department.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, code, name, active, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Department {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    active: ").append(toIndentedString(active)).append("\n");
    sb.append("    auditDetails: ").append(toIndentedString(auditDetails)).append("\n");
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

