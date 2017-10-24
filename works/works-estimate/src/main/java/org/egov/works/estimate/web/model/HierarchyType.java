package org.egov.works.estimate.web.model;

import java.time.LocalDate;
import java.util.Objects;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * HierarchyType
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-24T10:20:21.690Z")

public class HierarchyType {
	@JsonProperty("id")
	private String id = null;

	@JsonProperty("code")
	private String code = null;

	@JsonProperty("name")
	private LocalDate name = null;

	@JsonProperty("loalname")
	private LocalDate loalname = null;

	public HierarchyType id(String id) {
		this.id = id;
		return this;
	}

	/**
	 * unique id for the HierarchyType.
	 * 
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
	 * Unique Code for HierarchyType.Type
	 * 
	 * @return code
	 **/
	@ApiModelProperty(value = "Unique Code for HierarchyType.Type")

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public HierarchyType name(LocalDate name) {
		this.name = name;
		return this;
	}

	/**
	 * HierarchyType Name.
	 * 
	 * @return name
	 **/
	@ApiModelProperty(value = "HierarchyType Name.")

	@Valid

	public LocalDate getName() {
		return name;
	}

	public void setName(LocalDate name) {
		this.name = name;
	}

	public HierarchyType loalname(LocalDate loalname) {
		this.loalname = loalname;
		return this;
	}

	/**
	 * Local HierarchyType name .
	 * 
	 * @return loalname
	 **/
	@ApiModelProperty(value = "Local HierarchyType name .")

	@Valid

	public LocalDate getLoalname() {
		return loalname;
	}

	public void setLoalname(LocalDate loalname) {
		this.loalname = loalname;
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
		return Objects.equals(this.id, hierarchyType.id) && Objects.equals(this.code, hierarchyType.code)
				&& Objects.equals(this.name, hierarchyType.name)
				&& Objects.equals(this.loalname, hierarchyType.loalname);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, code, name, loalname);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class HierarchyType {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    code: ").append(toIndentedString(code)).append("\n");
		sb.append("    name: ").append(toIndentedString(name)).append("\n");
		sb.append("    loalname: ").append(toIndentedString(loalname)).append("\n");
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
