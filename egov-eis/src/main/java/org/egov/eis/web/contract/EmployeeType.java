package org.egov.eis.web.contract;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmployeeType {
	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("name")
	private String name = null;

	@JsonProperty("chartOfAccounts")
	private String chartOfAccounts = null;

	public EmployeeType id(Long id) {
		this.id = id;
		return this;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EmployeeType name(String name) {
		this.name = name;
		return this;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public EmployeeType chartOfAccounts(String chartOfAccounts) {
		this.chartOfAccounts = chartOfAccounts;
		return this;
	}

	public String getChartOfAccounts() {
		return chartOfAccounts;
	}

	public void setChartOfAccounts(String chartOfAccounts) {
		this.chartOfAccounts = chartOfAccounts;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		EmployeeType employeeType = (EmployeeType) o;
		return Objects.equals(this.id, employeeType.id) && Objects.equals(this.name, employeeType.name)
				&& Objects.equals(this.chartOfAccounts, employeeType.chartOfAccounts);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, chartOfAccounts);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class EmployeeType {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    name: ").append(toIndentedString(name)).append("\n");
		sb.append("    chartOfAccounts: ").append(toIndentedString(chartOfAccounts)).append("\n");
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
