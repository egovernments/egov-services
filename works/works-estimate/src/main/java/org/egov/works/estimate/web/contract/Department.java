package org.egov.works.estimate.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * Department
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-29T09:03:53.949Z")

public class Department   {
  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("code")
  private String code = null;

  @JsonProperty("active")
  private Boolean active = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  public Department id(Long id) {
    this.id = id;
    return this;
  }

   /**
   * Unique identifier of the department.
   * @return id
  **/
  @ApiModelProperty(value = "Unique identifier of the department.")


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Department name(String name) {
    this.name = name;
    return this;
  }

   /**
   * Unique Department name.
   * @return name
  **/
  @ApiModelProperty(required = true, value = "Unique Department name.")
  @NotNull

 @Size(min=8,max=64)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Department code(String code) {
    this.code = code;
    return this;
  }

   /**
   * Unique code of the department.
   * @return code
  **/
  @ApiModelProperty(required = true, value = "Unique code of the department.")
  @NotNull

 @Size(min=1,max=10)
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Department active(Boolean active) {
    this.active = active;
    return this;
  }

   /**
   * TRUE for active departments and FALSE for inactive departments.
   * @return active
  **/
  @ApiModelProperty(required = true, value = "TRUE for active departments and FALSE for inactive departments.")
  @NotNull


  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public Department tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Unique id for a tenant.
   * @return tenantId
  **/
  @ApiModelProperty(required = true, value = "Unique id for a tenant.")
  @NotNull


  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Department department = (Department) o;
    return Objects.equals(this.id, department.id) &&
        Objects.equals(this.name, department.name) &&
        Objects.equals(this.code, department.code) &&
        Objects.equals(this.active, department.active) &&
        Objects.equals(this.tenantId, department.tenantId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, code, active, tenantId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Department {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    active: ").append(toIndentedString(active)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
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

