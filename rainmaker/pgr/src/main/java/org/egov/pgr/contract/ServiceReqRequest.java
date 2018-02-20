package org.egov.pgr.contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.egov.common.contract.request.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * Request object to fetch the report data
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-02-20T05:39:55.235Z")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceReqRequest   {
  @JsonProperty("RequestInfo")
  private RequestInfo requestInfo = null;

  @JsonProperty("ServiceReq")
  private List<ServiceReq> serviceReq = new ArrayList<ServiceReq>();

  public ServiceReqRequest requestInfo(RequestInfo requestInfo) {
    this.requestInfo = requestInfo;
    return this;
  }

   /**
   * Get requestInfo
   * @return requestInfo
  **/
  @NotNull

  @Valid

  public RequestInfo getRequestInfo() {
    return requestInfo;
  }

  public void setRequestInfo(RequestInfo requestInfo) {
    this.requestInfo = requestInfo;
  }

  public ServiceReqRequest serviceReq(List<ServiceReq> serviceReq) {
    this.serviceReq = serviceReq;
    return this;
  }

  public ServiceReqRequest addServiceReqItem(ServiceReq serviceReqItem) {
    this.serviceReq.add(serviceReqItem);
    return this;
  }

   /**
   * Get serviceReq
   * @return serviceReq
  **/
  @NotNull

  @Valid

  public List<ServiceReq> getServiceReq() {
    return serviceReq;
  }

  public void setServiceReq(List<ServiceReq> serviceReq) {
    this.serviceReq = serviceReq;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ServiceReqRequest serviceReqRequest = (ServiceReqRequest) o;
    return Objects.equals(this.requestInfo, serviceReqRequest.requestInfo) &&
        Objects.equals(this.serviceReq, serviceReqRequest.serviceReq);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestInfo, serviceReq);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ServiceReqRequest {\n");
    
    sb.append("    requestInfo: ").append(toIndentedString(requestInfo)).append("\n");
    sb.append("    serviceReq: ").append(toIndentedString(serviceReq)).append("\n");
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

