package org.egov.pgr.contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.egov.common.contract.response.ResponseInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;


/**
 * Response to the metadata request
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-02-20T05:39:55.235Z")

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceReqResponse   {
  @JsonProperty("ResponseInfo")
  private ResponseInfo responseInfo = null;

  @JsonProperty("ServiceReq")
  private List<ServiceReq> serviceReq = new ArrayList<ServiceReq>();

  public ServiceReqResponse responseInfo(ResponseInfo responseInfo) {
    this.responseInfo = responseInfo;
    return this;
  }

   /**
   * Get responseInfo
   * @return responseInfo
  **/
  @NotNull

  @Valid

  public ResponseInfo getResponseInfo() {
    return responseInfo;
  }

  public void setResponseInfo(ResponseInfo responseInfo) {
    this.responseInfo = responseInfo;
  }

  public ServiceReqResponse serviceReq(List<ServiceReq> serviceReq) {
    this.serviceReq = serviceReq;
    return this;
  }

  public ServiceReqResponse addServiceReqItem(ServiceReq serviceReqItem) {
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
    ServiceReqResponse serviceReqResponse = (ServiceReqResponse) o;
    return Objects.equals(this.responseInfo, serviceReqResponse.responseInfo) &&
        Objects.equals(this.serviceReq, serviceReqResponse.serviceReq);
  }

  @Override
  public int hashCode() {
    return Objects.hash(responseInfo, serviceReq);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ServiceReqResponse {\n");
    
    sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
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

