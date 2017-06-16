package org.egov.models;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * This object the boundary info
 * Author : Narendra
 */

public class Boundary   {
	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("name")
	private String name = null;


	/**
	 * unique id of the Boundary.
	 * @return id
	 **/
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Name of the boundary.
	 * @return name
	 **/
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Boundary boundary = (Boundary) o;
		return Objects.equals(this.id, boundary.id) &&
				Objects.equals(this.name, boundary.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Boundary {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    name: ").append(toIndentedString(name)).append("\n");
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

