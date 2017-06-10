package org.egov.models;

import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * UsageMaster
 */

public class UsageMaster   {
	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("tenantId")
	@NotNull
	@Size(min=4,max=128)
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

	@JsonProperty("isResidential")
	private Boolean isResidential = null;

	@JsonProperty("orderNumber")
	private Integer orderNumber = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

	public UsageMaster id(Long id) {
		this.id = id;
		return this;
	}

	/**
	 * Unique Identifier of the UsageMaster
	 * @return id
	 **/
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UsageMaster tenantId(String tenantId) {
		this.tenantId = tenantId;
		return this;
	}

	/**
	 * tenant id of the UsageMaster
	 * @return tenantId
	 **/

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public UsageMaster name(String name) {
		this.name = name;
		return this;
	}

	/**
	 * name of the UsageMaster
	 * @return name
	 **/
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public UsageMaster code(String code) {
		this.code = code;
		return this;
	}

	/**
	 * code of the UsageMaster
	 * @return code
	 **/

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public UsageMaster nameLocal(String nameLocal) {
		this.nameLocal = nameLocal;
		return this;
	}

	/**
	 * name local of the UsageMaster
	 * @return nameLocal
	 **/

	public String getNameLocal() {
		return nameLocal;
	}

	public void setNameLocal(String nameLocal) {
		this.nameLocal = nameLocal;
	}

	public UsageMaster description(String description) {
		this.description = description;
		return this;
	}

	/**
	 * description of the UsageMaster
	 * @return description
	 **/

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public UsageMaster active(Boolean active) {
		this.active = active;
		return this;
	}

	/**
	 * Whether UsageMaster is Active or not. If the value is TRUE, then UsageMaster is active, if the value is FALSE then UsageMaster is inactive, default value is TRUE
	 * @return active
	 **/

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public UsageMaster isResidential(Boolean isResidential) {
		this.isResidential = isResidential;
		return this;
	}

	/**
	 * is residential of the UsageMaster
	 * @return isResidential
	 **/

	public Boolean getIsResidential() {
		return isResidential;
	}

	public void setIsResidential(Boolean isResidential) {
		this.isResidential = isResidential;
	}

	public UsageMaster orderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
		return this;
	}

	/**
	 * order number of the UsageMaster
	 * @return orderNumber
	 **/

	public Integer getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}

	public UsageMaster auditDetails(AuditDetails auditDetails) {
		this.auditDetails = auditDetails;
		return this;
	}

	/**
	 * Get auditDetails
	 * @return auditDetails
	 **/

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
		UsageMaster usageMaster = (UsageMaster) o;
		return Objects.equals(this.id, usageMaster.id) &&
				Objects.equals(this.tenantId, usageMaster.tenantId) &&
				Objects.equals(this.name, usageMaster.name) &&
				Objects.equals(this.code, usageMaster.code) &&
				Objects.equals(this.nameLocal, usageMaster.nameLocal) &&
				Objects.equals(this.description, usageMaster.description) &&
				Objects.equals(this.active, usageMaster.active) &&
				Objects.equals(this.isResidential, usageMaster.isResidential) &&
				Objects.equals(this.orderNumber, usageMaster.orderNumber) &&
				Objects.equals(this.auditDetails, usageMaster.auditDetails);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, tenantId, name, code, nameLocal, description, active, isResidential, orderNumber, auditDetails);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class UsageMaster {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
		sb.append("    name: ").append(toIndentedString(name)).append("\n");
		sb.append("    code: ").append(toIndentedString(code)).append("\n");
		sb.append("    nameLocal: ").append(toIndentedString(nameLocal)).append("\n");
		sb.append("    description: ").append(toIndentedString(description)).append("\n");
		sb.append("    active: ").append(toIndentedString(active)).append("\n");
		sb.append("    isResidential: ").append(toIndentedString(isResidential)).append("\n");
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


