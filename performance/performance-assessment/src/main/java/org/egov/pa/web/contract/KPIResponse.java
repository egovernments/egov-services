package org.egov.pa.web.contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.pa.model.KPI;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * KPIResponse
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-02T05:16:16.756Z")

public class KPIResponse   {
  @JsonProperty("responseInfo")
  private ResponseInfo responseInfo = null;

  @JsonProperty("KPIs")
  private List<KPI> kpIs = null;

  public KPIResponse responseInfo(ResponseInfo responseInfo) {
    this.responseInfo = responseInfo;
    return this;
  }

   /**
   * Get responseInfo
   * @return responseInfo
  **/

  @Valid

  public ResponseInfo getResponseInfo() {
    return responseInfo;
  }

  public void setResponseInfo(ResponseInfo responseInfo) {
    this.responseInfo = responseInfo;
  }

  public KPIResponse kpIs(List<KPI> kpIs) {
    this.kpIs = kpIs;
    return this;
  }

  public KPIResponse addKpIsItem(KPI kpIsItem) {
    if (this.kpIs == null) {
      this.kpIs = new ArrayList<KPI>();
    }
    this.kpIs.add(kpIsItem);
    return this;
  }

   /**
   * Get kpIs
   * @return kpIs
  **/

  @Valid

  public List<KPI> getKpIs() {
    return kpIs;
  }

  public void setKpIs(List<KPI> kpIs) {
    this.kpIs = kpIs;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    KPIResponse kpIResponse = (KPIResponse) o;
    return Objects.equals(this.responseInfo, kpIResponse.responseInfo) &&
        Objects.equals(this.kpIs, kpIResponse.kpIs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(responseInfo, kpIs);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class KPIResponse {\n");
    
    sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
    sb.append("    kpIs: ").append(toIndentedString(kpIs)).append("\n");
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

