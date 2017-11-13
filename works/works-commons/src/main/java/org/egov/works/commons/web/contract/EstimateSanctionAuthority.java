package org.egov.works.commons.web.contract;

import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * An Object that holds Sanction Authority details for Estimate
 */
@ApiModel(description = "An Object that holds Sanction Authority details for Estimate")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-08T13:25:44.581Z")

public class EstimateSanctionAuthority {
	@JsonProperty("id")
	private String id = null;

	@JsonProperty("tenantId")
	private String tenantId = null;

	@JsonProperty("name")
	private String name = null;

	public EstimateSanctionAuthority id(String id) {
		this.id = id;
		return this;
	}

	/**
	 * Unique Identifier of the Estimate Sanction Authority
	 * 
	 * @return id
	 **/
	@ApiModelProperty(value = "Unique Identifier of the Estimate Sanction Authority")

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public EstimateSanctionAuthority tenantId(String tenantId) {
		this.tenantId = tenantId;
		return this;
	}

	/**
	 * Tenant id of the Estimate Sanction Authority
	 * 
	 * @return tenantId
	 **/
	@ApiModelProperty(required = true, value = "Tenant id of the Estimate Sanction Authority")
	@NotNull

	@Size(min = 2, max = 128)
	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public EstimateSanctionAuthority name(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Name of the Estimate Sanction Authority
	 * 
	 * @return name
	 **/
	@ApiModelProperty(required = true, value = "Name of the Estimate Sanction Authority")
	@NotNull

	@Pattern(regexp = "[a-zA-Z0-9\\s\\.,]+")
	@Size(min = 1, max = 100)
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
		EstimateSanctionAuthority estimateSanctionAuthority = (EstimateSanctionAuthority) o;
		return Objects.equals(this.id, estimateSanctionAuthority.id)
				&& Objects.equals(this.tenantId, estimateSanctionAuthority.tenantId)
				&& Objects.equals(this.name, estimateSanctionAuthority.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, tenantId, name);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class EstimateSanctionAuthority {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
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
