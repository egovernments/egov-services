package org.egov.works.services.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * BoundaryType
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-25T07:37:26.972Z")

public class BoundaryType   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("hierarchy")
  private Integer hierarchy = null;

  @JsonProperty("code")
  private String code = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("parent")
  private BoundaryType parent = null;

  @JsonProperty("hierarchyType")
  private HierarchyType hierarchyType = null;

  public BoundaryType id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique id of the Boundary Type.
   * @return id
  **/
  @ApiModelProperty(value = "Unique id of the Boundary Type.")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public BoundaryType tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * tenantId of the BoundaryType.
   * @return tenantId
  **/
  @ApiModelProperty(required = true, value = "tenantId of the BoundaryType.")
  @NotNull


  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public BoundaryType hierarchy(Integer hierarchy) {
    this.hierarchy = hierarchy;
    return this;
  }

   /**
   * hierarchy of the BoundaryType.
   * @return hierarchy
  **/
  @ApiModelProperty(required = true, value = "hierarchy of the BoundaryType.")
  @NotNull


  public Integer getHierarchy() {
    return hierarchy;
  }

  public void setHierarchy(Integer hierarchy) {
    this.hierarchy = hierarchy;
  }

  public BoundaryType code(String code) {
    this.code = code;
    return this;
  }

   /**
   * unique Code of the BoundaryType.
   * @return code
  **/
  @ApiModelProperty(required = true, value = "unique Code of the BoundaryType.")
  @NotNull


  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public BoundaryType name(String name) {
    this.name = name;
    return this;
  }

   /**
   * Get name
   * @return name
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BoundaryType parent(BoundaryType parent) {
    this.parent = parent;
    return this;
  }

   /**
   * Get parent
   * @return parent
  **/
  @ApiModelProperty(value = "")

  @Valid

  public BoundaryType getParent() {
    return parent;
  }

  public void setParent(BoundaryType parent) {
    this.parent = parent;
  }

  public BoundaryType hierarchyType(HierarchyType hierarchyType) {
    this.hierarchyType = hierarchyType;
    return this;
  }

   /**
   * Get hierarchyType
   * @return hierarchyType
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public HierarchyType getHierarchyType() {
    return hierarchyType;
  }

  public void setHierarchyType(HierarchyType hierarchyType) {
    this.hierarchyType = hierarchyType;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BoundaryType boundaryType = (BoundaryType) o;
    return Objects.equals(this.id, boundaryType.id) &&
        Objects.equals(this.tenantId, boundaryType.tenantId) &&
        Objects.equals(this.hierarchy, boundaryType.hierarchy) &&
        Objects.equals(this.code, boundaryType.code) &&
        Objects.equals(this.name, boundaryType.name) &&
        Objects.equals(this.parent, boundaryType.parent) &&
        Objects.equals(this.hierarchyType, boundaryType.hierarchyType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, hierarchy, code, name, parent, hierarchyType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BoundaryType {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    hierarchy: ").append(toIndentedString(hierarchy)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    parent: ").append(toIndentedString(parent)).append("\n");
    sb.append("    hierarchyType: ").append(toIndentedString(hierarchyType)).append("\n");
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

