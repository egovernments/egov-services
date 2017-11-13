package org.egov.lams.common.web.contract;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * HierarchyType
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-09T07:10:49.937Z")

public class HierarchyType   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("code")
  private String code = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("localName")
  private String localName = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  public HierarchyType id(String id) {
    this.id = id;
    return this;
  }

   /**
   * unique id for the HierarchyType.
   * @return id
  **/
  @ApiModelProperty(value = "unique id for the HierarchyType.")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public HierarchyType code(String code) {
    this.code = code;
    return this;
  }

   /**
   * Unique Code for HierarchyType.
   * @return code
  **/
  @ApiModelProperty(required = true, value = "Unique Code for HierarchyType.")
  @NotNull


  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public HierarchyType name(String name) {
    this.name = name;
    return this;
  }

   /**
   * HierarchyType Name.
   * @return name
  **/
  @ApiModelProperty(required = true, value = "HierarchyType Name.")
  @NotNull


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public HierarchyType localName(String localName) {
    this.localName = localName;
    return this;
  }

   /**
   * Local HierarchyType name
   * @return localName
  **/
  @ApiModelProperty(value = "Local HierarchyType name")


  public String getLocalName() {
    return localName;
  }

  public void setLocalName(String localName) {
    this.localName = localName;
  }

  public HierarchyType tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Unique TenantId for HierarchyType .
   * @return tenantId
  **/
  @ApiModelProperty(required = true, value = "Unique TenantId for HierarchyType .")
  @NotNull


  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HierarchyType hierarchyType = (HierarchyType) o;
    return Objects.equals(this.id, hierarchyType.id) &&
        Objects.equals(this.code, hierarchyType.code) &&
        Objects.equals(this.name, hierarchyType.name) &&
        Objects.equals(this.localName, hierarchyType.localName) &&
        Objects.equals(this.tenantId, hierarchyType.tenantId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, code, name, localName, tenantId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HierarchyType {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    localName: ").append(toIndentedString(localName)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
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

