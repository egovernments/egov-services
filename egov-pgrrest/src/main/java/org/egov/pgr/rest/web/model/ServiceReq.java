package org.egov.pgr.rest.web.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceReq   {
  @JsonProperty("RequestInfo")
  private RequestInfo requestInfo = null;

  @JsonProperty("Service")
  private Service service = null;

  public ServiceReq requestInfo(RequestInfo requestInfo) {
    this.requestInfo = requestInfo;
    return this;
  }

  public RequestInfo getRequestInfo() {
    return requestInfo;
  }

  public void setRequestInfo(RequestInfo requestInfo) {
    this.requestInfo = requestInfo;
  }

  public ServiceReq service(Service service) {
    this.service = service;
    return this;
  }

  public Service getService() {
    return service;
  }

  public void setService(Service service) {
    this.service = service;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ServiceReq serviceReq = (ServiceReq) o;
    return Objects.equals(this.requestInfo, serviceReq.requestInfo) &&
        Objects.equals(this.service, serviceReq.service);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestInfo, service);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ServiceReq {\n");
    
    sb.append("    requestInfo: ").append(toIndentedString(requestInfo)).append("\n");
    sb.append("    service: ").append(toIndentedString(service)).append("\n");
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