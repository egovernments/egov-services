package org.egov.pgr.rest.web.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
public class ServiceRes   {
  @JsonProperty("ResposneInfo")
  private ResponseInfo resposneInfo = null;

  @JsonProperty("Services")
  private List<Service> services = new ArrayList<>();

  public ServiceRes resposneInfo(ResponseInfo resposneInfo) {
    this.resposneInfo = resposneInfo;
    return this;
  }

  public ResponseInfo getResposneInfo() {
    return resposneInfo;
  }

  public void setResposneInfo(ResponseInfo resposneInfo) {
    this.resposneInfo = resposneInfo;
  }

  public ServiceRes services(List<Service> services) {
    this.services = services;
    return this;
  }

  public ServiceRes addServicesItem(Service servicesItem) {
    this.services.add(servicesItem);
    return this;
  }

  public List<Service> getServices() {
    return services;
  }

  public void setServices(List<Service> services) {
    this.services = services;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ServiceRes serviceRes = (ServiceRes) o;
    return Objects.equals(this.resposneInfo, serviceRes.resposneInfo) &&
        Objects.equals(this.services, serviceRes.services);
  }

  @Override
  public int hashCode() {
    return Objects.hash(resposneInfo, services);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ServiceRes {\n");
    
    sb.append("    resposneInfo: ").append(toIndentedString(resposneInfo)).append("\n");
    sb.append("    services: ").append(toIndentedString(services)).append("\n");
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