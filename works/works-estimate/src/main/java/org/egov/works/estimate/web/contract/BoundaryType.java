package org.egov.works.estimate.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * BoundaryType
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-29T09:03:53.949Z")

public class BoundaryType   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("code")
  private String code = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("hierarchy")
  private Integer hierarchy = null;

  @JsonProperty("localName")
  private String localName = null;

  @JsonProperty("parentName")
  private String parentName = null;

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

  public BoundaryType localName(String localName) {
    this.localName = localName;
    return this;
  }

   /**
   * localName of the BoundaryType.
   * @return localName
  **/
  @ApiModelProperty(value = "localName of the BoundaryType.")


  public String getLocalName() {
    return localName;
  }

  public void setLocalName(String localName) {
    this.localName = localName;
  }

  public BoundaryType parentName(String parentName) {
    this.parentName = parentName;
    return this;
  }

   /**
   * parentName of the BoundaryType.
   * @return parentName
  **/
  @ApiModelProperty(value = "parentName of the BoundaryType.")


  public String getParentName() {
    return parentName;
  }

  public void setParentName(String parentName) {
    this.parentName = parentName;
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
        Objects.equals(this.name, boundaryType.name) &&
        Objects.equals(this.code, boundaryType.code) &&
        Objects.equals(this.tenantId, boundaryType.tenantId) &&
        Objects.equals(this.hierarchy, boundaryType.hierarchy) &&
        Objects.equals(this.localName, boundaryType.localName) &&
        Objects.equals(this.parentName, boundaryType.parentName) &&
        Objects.equals(this.parent, boundaryType.parent) &&
        Objects.equals(this.hierarchyType, boundaryType.hierarchyType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, code, tenantId, hierarchy, localName, parentName, parent, hierarchyType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BoundaryType {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    hierarchy: ").append(toIndentedString(hierarchy)).append("\n");
    sb.append("    localName: ").append(toIndentedString(localName)).append("\n");
    sb.append("    parentName: ").append(toIndentedString(parentName)).append("\n");
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

