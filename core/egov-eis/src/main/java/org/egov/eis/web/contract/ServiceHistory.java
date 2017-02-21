package org.egov.eis.web.contract;

import java.util.Objects;

import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceHistory {
	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("serviceInfo")
	private String serviceInfo = null;

	@JsonProperty("serviceFrom")
	private LocalDate serviceFrom = null;

	@JsonProperty("remarks")
	private String remarks = null;

	public ServiceHistory id(Long id) {
		this.id = id;
		return this;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ServiceHistory serviceInfo(String serviceInfo) {
		this.serviceInfo = serviceInfo;
		return this;
	}

	public String getServiceInfo() {
		return serviceInfo;
	}

	public void setServiceInfo(String serviceInfo) {
		this.serviceInfo = serviceInfo;
	}

	public ServiceHistory serviceFrom(LocalDate serviceFrom) {
		this.serviceFrom = serviceFrom;
		return this;
	}

	public LocalDate getServiceFrom() {
		return serviceFrom;
	}

	public void setServiceFrom(LocalDate serviceFrom) {
		this.serviceFrom = serviceFrom;
	}

	public ServiceHistory remarks(String remarks) {
		this.remarks = remarks;
		return this;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ServiceHistory serviceHistory = (ServiceHistory) o;
		return Objects.equals(this.id, serviceHistory.id)
				&& Objects.equals(this.serviceInfo, serviceHistory.serviceInfo)
				&& Objects.equals(this.serviceFrom, serviceHistory.serviceFrom)
				&& Objects.equals(this.remarks, serviceHistory.remarks);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, serviceInfo, serviceFrom, remarks);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ServiceHistory {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    serviceInfo: ").append(toIndentedString(serviceInfo)).append("\n");
		sb.append("    serviceFrom: ").append(toIndentedString(serviceFrom)).append("\n");
		sb.append("    remarks: ").append(toIndentedString(remarks)).append("\n");
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
