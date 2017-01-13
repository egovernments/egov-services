package org.egov.pgr.rest.web.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceDefinitionRes   {
  @JsonProperty("ResposneInfo")
  private ResponseInfo resposneInfo = null;

  @JsonProperty("ServiceDefinition")
  private ServiceDefinition serviceDefinition = null;

  public ServiceDefinitionRes resposneInfo(ResponseInfo resposneInfo) {
    this.resposneInfo = resposneInfo;
    return this;
  }

  public ResponseInfo getResposneInfo() {
    return resposneInfo;
  }

  public void setResposneInfo(ResponseInfo resposneInfo) {
    this.resposneInfo = resposneInfo;
  }

  public ServiceDefinitionRes serviceDefinition(ServiceDefinition serviceDefinition) {
    this.serviceDefinition = serviceDefinition;
    return this;
  }

  public ServiceDefinition getServiceDefinition() {
    return serviceDefinition;
  }

  public void setServiceDefinition(ServiceDefinition serviceDefinition) {
    this.serviceDefinition = serviceDefinition;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ServiceDefinitionRes serviceDefinitionRes = (ServiceDefinitionRes) o;
    return Objects.equals(this.resposneInfo, serviceDefinitionRes.resposneInfo) &&
        Objects.equals(this.serviceDefinition, serviceDefinitionRes.serviceDefinition);
  }

  @Override
  public int hashCode() {
    return Objects.hash(resposneInfo, serviceDefinition);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ServiceDefinitionRes {\n");
    
    sb.append("    resposneInfo: ").append(toIndentedString(resposneInfo)).append("\n");
    sb.append("    serviceDefinition: ").append(toIndentedString(serviceDefinition)).append("\n");
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

