package org.egov.works.commons.web.contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * An Object that holds Overhead master data.
 */
@ApiModel(description = "An Object that holds Overhead master data.")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-08T13:25:44.581Z")

public class Overhead {
	@JsonProperty("id")
	private String id = null;

	@JsonProperty("tenantId")
	private String tenantId = null;

	@JsonProperty("name")
	private String name = null;

	@JsonProperty("code")
	private String code = null;

	@JsonProperty("accountCode")
	private String accountCode = null;

	@JsonProperty("description")
	private String description = null;

	@JsonProperty("overheadRate")
	private List<OverheadRate> overheadRate = new ArrayList<OverheadRate>();

	public Overhead id(String id) {
		this.id = id;
		return this;
	}

	/**
	 * Unique Identifier of the Overhead
	 * 
	 * @return id
	 **/
	@ApiModelProperty(value = "Unique Identifier of the Overhead")

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Overhead tenantId(String tenantId) {
		this.tenantId = tenantId;
		return this;
	}

	/**
	 * Tenant id of the Overhead
	 * 
	 * @return tenantId
	 **/
	@ApiModelProperty(required = true, value = "Tenant id of the Overhead")
	@NotNull

	@Size(min = 2, max = 128)
	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public Overhead name(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Name of the Overhead
	 * 
	 * @return name
	 **/
	@ApiModelProperty(required = true, value = "Name of the Overhead")
	@NotNull

	@Pattern(regexp = "[a-zA-Z0-9\\s\\.,]+")
	@Size(min = 1, max = 100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Overhead code(String code) {
		this.code = code;
		return this;
	}

	/**
	 * Name of the Overhead
	 * 
	 * @return code
	 **/
	@ApiModelProperty(required = true, value = "Name of the Overhead")
	@NotNull

	@Pattern(regexp = "[a-zA-Z0-9-\\\\]+")
	@Size(min = 1, max = 100)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Overhead accountCode(String accountCode) {
		this.accountCode = accountCode;
		return this;
	}

	/**
	 * Account code list should be fetch from Chart of accounts master of
	 * Finance Module
	 * 
	 * @return accountCode
	 **/
	@ApiModelProperty(required = true, value = "Account code list should be fetch from Chart of accounts master of Finance Module")
	@NotNull

	@Size(max = 100)
	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}

	public Overhead description(String description) {
		this.description = description;
		return this;
	}

	/**
	 * Description of the Overhead
	 * 
	 * @return description
	 **/
	@ApiModelProperty(value = "Description of the Overhead")

	@Pattern(regexp = "[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]+")
	@Size(max = 1024)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Overhead overheadRate(List<OverheadRate> overheadRate) {
		this.overheadRate = overheadRate;
		return this;
	}

	public Overhead addOverheadRateItem(OverheadRate overheadRateItem) {
		this.overheadRate.add(overheadRateItem);
		return this;
	}

	/**
	 * Array of Overhead Rate Details
	 * 
	 * @return overheadRate
	 **/
	@ApiModelProperty(required = true, value = "Array of Overhead Rate Details")
	@NotNull

	@Valid

	public List<OverheadRate> getOverheadRate() {
		return overheadRate;
	}

	public void setOverheadRate(List<OverheadRate> overheadRate) {
		this.overheadRate = overheadRate;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Overhead overhead = (Overhead) o;
		return Objects.equals(this.id, overhead.id) && Objects.equals(this.tenantId, overhead.tenantId)
				&& Objects.equals(this.name, overhead.name) && Objects.equals(this.code, overhead.code)
				&& Objects.equals(this.accountCode, overhead.accountCode)
				&& Objects.equals(this.description, overhead.description)
				&& Objects.equals(this.overheadRate, overhead.overheadRate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, tenantId, name, code, accountCode, description, overheadRate);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Overhead {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
		sb.append("    name: ").append(toIndentedString(name)).append("\n");
		sb.append("    code: ").append(toIndentedString(code)).append("\n");
		sb.append("    accountCode: ").append(toIndentedString(accountCode)).append("\n");
		sb.append("    description: ").append(toIndentedString(description)).append("\n");
		sb.append("    overheadRate: ").append(toIndentedString(overheadRate)).append("\n");
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
