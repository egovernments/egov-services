package org.egov.eis.web.contract;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Designation {
	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("name")
	private String name = null;

	@JsonProperty("code")
	private String code = null;

	@JsonProperty("description")
	private String description = null;

	@JsonProperty("chartOfAccounts")
	private String chartOfAccounts = null;

	@JsonProperty("active")
	private Boolean active = null;

	public Designation id(Long id) {
		this.id = id;
		return this;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Designation name(String name) {
		this.name = name;
		return this;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Designation code(String code) {
		this.code = code;
		return this;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Designation description(String description) {
		this.description = description;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Designation chartOfAccounts(String chartOfAccounts) {
		this.chartOfAccounts = chartOfAccounts;
		return this;
	}

	public String getChartOfAccounts() {
		return chartOfAccounts;
	}

	public void setChartOfAccounts(String chartOfAccounts) {
		this.chartOfAccounts = chartOfAccounts;
	}

	public Designation active(Boolean active) {
		this.active = active;
		return this;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Designation designation = (Designation) o;
		return Objects.equals(this.id, designation.id) && Objects.equals(this.name, designation.name)
				&& Objects.equals(this.code, designation.code)
				&& Objects.equals(this.description, designation.description)
				&& Objects.equals(this.chartOfAccounts, designation.chartOfAccounts)
				&& Objects.equals(this.active, designation.active);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, code, description, chartOfAccounts, active);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Designation {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    name: ").append(toIndentedString(name)).append("\n");
		sb.append("    code: ").append(toIndentedString(code)).append("\n");
		sb.append("    description: ").append(toIndentedString(description)).append("\n");
		sb.append("    chartOfAccounts: ").append(toIndentedString(chartOfAccounts)).append("\n");
		sb.append("    active: ").append(toIndentedString(active)).append("\n");
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
