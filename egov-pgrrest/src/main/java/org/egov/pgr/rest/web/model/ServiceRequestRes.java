package org.egov.pgr.rest.web.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceRequestRes   {
  @JsonProperty("ResposneInfo")
  private ResponseInfo resposneInfo = null;

  @JsonProperty("ServiceRequests")
  private List<ServiceRequest> serviceRequests = new ArrayList<>();

  public ServiceRequestRes resposneInfo(ResponseInfo resposneInfo) {
    this.resposneInfo = resposneInfo;
    return this;
  }

  public ResponseInfo getResposneInfo() {
    return resposneInfo;
  }

  public void setResposneInfo(ResponseInfo resposneInfo) {
    this.resposneInfo = resposneInfo;
  }

  public ServiceRequestRes serviceRequests(List<ServiceRequest> serviceRequests) {
    this.serviceRequests = serviceRequests;
    return this;
  }

  public ServiceRequestRes addServiceRequestsItem(ServiceRequest serviceRequestsItem) {
    this.serviceRequests.add(serviceRequestsItem);
    return this;
  }

  public List<ServiceRequest> getServiceRequests() {
    return serviceRequests;
  }

  public void setServiceRequests(List<ServiceRequest> serviceRequests) {
    this.serviceRequests = serviceRequests;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ServiceRequestRes serviceRequestRes = (ServiceRequestRes) o;
    return Objects.equals(this.resposneInfo, serviceRequestRes.resposneInfo) &&
        Objects.equals(this.serviceRequests, serviceRequestRes.serviceRequests);
  }

  @Override
  public int hashCode() {
    return Objects.hash(resposneInfo, serviceRequests);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ServiceRequestRes {\n");
    
    sb.append("    resposneInfo: ").append(toIndentedString(resposneInfo)).append("\n");
    sb.append("    serviceRequests: ").append(toIndentedString(serviceRequests)).append("\n");
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