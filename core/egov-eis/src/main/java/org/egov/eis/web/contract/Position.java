package org.egov.eis.web.contract;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Position {
	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("name")
	private String name = null;

	@JsonProperty("deptdesig")
	private Object deptdesig = null;

	@JsonProperty("isPostOutsourced")
	private Boolean isPostOutsourced = null;

	@JsonProperty("tenantId")
	private String tenantId = null;

	public Position id(Long id) {
		this.id = id;
		return this;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Position name(String name) {
		this.name = name;
		return this;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Position deptdesig(Object deptdesig) {
		this.deptdesig = deptdesig;
		return this;
	}

	public Object getDeptdesig() {
		return deptdesig;
	}

	public void setDeptdesig(Object deptdesig) {
		this.deptdesig = deptdesig;
	}

	public Position isPostOutsourced(Boolean isPostOutsourced) {
		this.isPostOutsourced = isPostOutsourced;
		return this;
	}

	public Boolean getIsPostOutsourced() {
		return isPostOutsourced;
	}

	public void setIsPostOutsourced(Boolean isPostOutsourced) {
		this.isPostOutsourced = isPostOutsourced;
	}

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
		Position position = (Position) o;
		return Objects.equals(this.id, position.id) && Objects.equals(this.name, position.name)
				&& Objects.equals(this.deptdesig, position.deptdesig)
				&& Objects.equals(this.tenantId, position.tenantId)
				&& Objects.equals(this.isPostOutsourced, position.isPostOutsourced);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, deptdesig, isPostOutsourced, tenantId);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Position {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    name: ").append(toIndentedString(name)).append("\n");
		sb.append("    deptdesig: ").append(toIndentedString(deptdesig)).append("\n");
		sb.append("    isPostOutsourced: ").append(toIndentedString(isPostOutsourced)).append("\n");
		sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	private String toIndentedString(java.lang.Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}
