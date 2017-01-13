package org.egov.pgr.web.validation.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceRequestReq {
	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo = null;

	@JsonProperty("ServiceRequest")
	private ServiceRequest serviceRequest = null;

	public ServiceRequestReq requestInfo(RequestInfo requestInfo) {
		this.requestInfo = requestInfo;
		return this;
	}

	public RequestInfo getRequestInfo() {
		return requestInfo;
	}

	public void setRequestInfo(RequestInfo requestInfo) {
		this.requestInfo = requestInfo;
	}

	public ServiceRequestReq serviceRequest(ServiceRequest serviceRequest) {
		this.serviceRequest = serviceRequest;
		return this;
	}

	public ServiceRequest getServiceRequest() {
		return serviceRequest;
	}

	public void setServiceRequest(ServiceRequest serviceRequest) {
		this.serviceRequest = serviceRequest;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ServiceRequestReq serviceRequestReq = (ServiceRequestReq) o;
		return Objects.equals(this.requestInfo, serviceRequestReq.requestInfo)
				&& Objects.equals(this.serviceRequest, serviceRequestReq.serviceRequest);
	}

	@Override
	public int hashCode() {
		return Objects.hash(requestInfo, serviceRequest);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ServiceRequestReq {\n");

		sb.append("    requestInfo: ").append(toIndentedString(requestInfo)).append("\n");
		sb.append("    serviceRequest: ").append(toIndentedString(serviceRequest)).append("\n");
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