package org.egov.tradelicense.web.requests;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.egov.tradelicense.domain.model.TradeLicense;

import com.fasterxml.jackson.annotation.JsonProperty;


public class TradeLicenseRequest   {
  @JsonProperty("requestInfo")
  private RequestInfo requestInfo = null;

  @JsonProperty("licesnses")
  private List<TradeLicense> licesnses = null;

  public TradeLicenseRequest requestInfo(RequestInfo requestInfo) {
    this.requestInfo = requestInfo;
    return this;
  }


  @Valid
  public RequestInfo getRequestInfo() {
    return requestInfo;
  }

  public void setRequestInfo(RequestInfo requestInfo) {
    this.requestInfo = requestInfo;
  }

  public TradeLicenseRequest licesnses(List<TradeLicense> licesnses) {
    this.licesnses = licesnses;
    return this;
  }

  public TradeLicenseRequest addLicesnsesItem(TradeLicense licesnsesItem) {
    if (this.licesnses == null) {
      this.licesnses = new ArrayList<TradeLicense>();
    }
    this.licesnses.add(licesnsesItem);
    return this;
  }

  @Valid
  public List<TradeLicense> getLicesnses() {
    return licesnses;
  }

  public void setLicesnses(List<TradeLicense> licesnses) {
    this.licesnses = licesnses;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TradeLicenseRequest tradeLicenseRequest = (TradeLicenseRequest) o;
    return Objects.equals(this.requestInfo, tradeLicenseRequest.requestInfo) &&
        Objects.equals(this.licesnses, tradeLicenseRequest.licesnses);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestInfo, licesnses);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TradeLicenseRequest {\n");
    
    sb.append("    requestInfo: ").append(toIndentedString(requestInfo)).append("\n");
    sb.append("    licesnses: ").append(toIndentedString(licesnses)).append("\n");
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

