package org.egov.models;

import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * StructureClass
 */

public class StructureClass   {
	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("tenantId")
	@NotNull
	@Size(min=8,max=128)
	private String tenantId = null;

	@JsonProperty("name")
	@NotNull
	@Size(min=4,max=128)
	private String name = null;

	@JsonProperty("code")
	@NotNull
	@Size(min=4,max=16)
	private String code = null;

	@JsonProperty("nameLocal")
	@Size(min=8,max=256)
	private String nameLocal = null;

	@JsonProperty("description")
	@Size(min=8,max=512)
	private String description = null;

	@JsonProperty("active")
	private Boolean active = null;

	@JsonProperty("orderNumber")
	private Integer orderNumber = null;

	@JsonIgnore
	@JsonProperty("data")
	private String data;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public StructureClass id(Long id) {
		this.id = id;
		return this;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public StructureClass tenantId(String tenantId) {
		this.tenantId = tenantId;
		return this;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public StructureClass name(String name) {
		this.name = name;
		return this;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public StructureClass code(String code) {
		this.code = code;
		return this;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public StructureClass nameLocal(String nameLocal) {
		this.nameLocal = nameLocal;
		return this;
	}

	public String getNameLocal() {
		return nameLocal;
	}

	public void setNameLocal(String nameLocal) {
		this.nameLocal = nameLocal;
	}

	public StructureClass description(String description) {
		this.description = description;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public StructureClass active(Boolean active) {
		this.active = active;
		return this;
	}


	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public StructureClass orderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
		return this;
	}


	public Integer getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}

	public StructureClass auditDetails(AuditDetails auditDetails) {
		this.auditDetails = auditDetails;
		return this;
	}


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
		StructureClass structureClass = (StructureClass) o;
		return Objects.equals(this.id, structureClass.id) &&
				Objects.equals(this.tenantId, structureClass.tenantId) &&
				Objects.equals(this.name, structureClass.name) &&
				Objects.equals(this.code, structureClass.code) &&
				Objects.equals(this.nameLocal, structureClass.nameLocal) &&
				Objects.equals(this.description, structureClass.description) &&
				Objects.equals(this.active, structureClass.active) &&
				Objects.equals(this.orderNumber, structureClass.orderNumber) &&
				Objects.equals(this.auditDetails, structureClass.auditDetails);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, tenantId, name, code, nameLocal, description, active, orderNumber, auditDetails);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class StructureClass {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
		sb.append("    name: ").append(toIndentedString(name)).append("\n");
		sb.append("    code: ").append(toIndentedString(code)).append("\n");
		sb.append("    nameLocal: ").append(toIndentedString(nameLocal)).append("\n");
		sb.append("    description: ").append(toIndentedString(description)).append("\n");
		sb.append("    active: ").append(toIndentedString(active)).append("\n");
		sb.append("    orderNumber: ").append(toIndentedString(orderNumber)).append("\n");
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


