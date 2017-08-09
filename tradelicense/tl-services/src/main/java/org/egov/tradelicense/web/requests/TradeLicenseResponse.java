package org.egov.tradelicense.web.requests;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;

import org.egov.tradelicense.domain.model.TradeLicense;

import com.fasterxml.jackson.annotation.JsonProperty;


public class TradeLicenseResponse   {
  @JsonProperty("responseInfo")
  private ResponseInfo responseInfo = null;

  @JsonProperty("licenses")
  private List<TradeLicense> licenses = null;

  public TradeLicenseResponse responseInfo(ResponseInfo responseInfo) {
    this.responseInfo = responseInfo;
    return this;
  }

  @Valid
  public ResponseInfo getResponseInfo() {
    return responseInfo;
  }

  public void setResponseInfo(ResponseInfo responseInfo) {
    this.responseInfo = responseInfo;
  }

  public TradeLicenseResponse licenses(List<TradeLicense> licenses) {
    this.licenses = licenses;
    return this;
  }

  public TradeLicenseResponse addLicensesItem(TradeLicense licensesItem) {
    if (this.licenses == null) {
      this.licenses = new ArrayList<TradeLicense>();
    }
    this.licenses.add(licensesItem);
    return this;
  }


  @Valid
  public List<TradeLicense> getLicenses() {
    return licenses;
  }

  public void setLicenses(List<TradeLicense> licenses) {
    this.licenses = licenses;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TradeLicenseResponse tradeLicenseResponse = (TradeLicenseResponse) o;
    return Objects.equals(this.responseInfo, tradeLicenseResponse.responseInfo) &&
        Objects.equals(this.licenses, tradeLicenseResponse.licenses);
  }

  @Override
  public int hashCode() {
    return Objects.hash(responseInfo, licenses);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TradeLicenseResponse {\n");
    
    sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
    sb.append("    licenses: ").append(toIndentedString(licenses)).append("\n");
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

