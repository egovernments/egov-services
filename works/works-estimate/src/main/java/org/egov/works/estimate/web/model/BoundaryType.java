package org.egov.works.estimate.web.model;

import java.time.LocalDate;
import java.util.Objects;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * BoundaryType
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-24T10:20:21.690Z")

public class BoundaryType {
	@JsonProperty("id")
	private String id = null;

	@JsonProperty("name")
	private LocalDate name = null;

	@JsonProperty("parent")
	private BoundaryType parent = null;

	@JsonProperty("hierarchyType")
	private HierarchyType hierarchyType = null;

	public BoundaryType id(String id) {
		this.id = id;
		return this;
	}

	/**
	 * boundary number of the boundary.
	 * 
	 * @return id
	 **/
	@ApiModelProperty(value = "boundary number of the boundary.")

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BoundaryType name(LocalDate name) {
		this.name = name;
		return this;
	}

	/**
	 * Get name
	 * 
	 * @return name
	 **/
	@ApiModelProperty(value = "")

	@Valid

	public LocalDate getName() {
		return name;
	}

	public void setName(LocalDate name) {
		this.name = name;
	}

	public BoundaryType parent(BoundaryType parent) {
		this.parent = parent;
		return this;
	}

	/**
	 * Get parent
	 * 
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
	 * 
	 * @return hierarchyType
	 **/
	@ApiModelProperty(value = "")

	@Valid

	public HierarchyType getHierarchyType() {
		return hierarchyType;
	}

	public void setHierarchyType(HierarchyType hierarchyType) {
		this.hierarchyType = hierarchyType;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		BoundaryType boundaryType = (BoundaryType) o;
		return Objects.equals(this.id, boundaryType.id) && Objects.equals(this.name, boundaryType.name)
				&& Objects.equals(this.parent, boundaryType.parent)
				&& Objects.equals(this.hierarchyType, boundaryType.hierarchyType);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, parent, hierarchyType);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class BoundaryType {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
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
	private String toIndentedString(java.lang.Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}
