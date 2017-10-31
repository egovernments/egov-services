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
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-25T11:25:20.008Z")

public class KPIResponse   {
  @JsonProperty("ResponseInfo")
  private ResponseInfo responseInfo = null;

  @JsonProperty("KPIs")
  private List<KPI> kpis = null;

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

  public KPIResponse kpis(List<KPI> kpis) {
    this.kpis = kpis;
    return this;
  }

  public KPIResponse addKpisItem(KPI kpIsItem) {
    if (this.kpis == null) {
      this.kpis = new ArrayList<KPI>();
    }
    this.kpis.add(kpIsItem);
    return this;
  }

   /**
   * Get kpIs
   * @return kpIs
  **/

  @Valid

  public List<KPI> getKpis() {
    return kpis;
  }

  public void setKpis(List<KPI> kpis) {
    this.kpis = kpis;
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
        Objects.equals(this.kpis, kpIResponse.kpis);
  }

  @Override
  public int hashCode() {
    return Objects.hash(responseInfo, kpis);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class KPIResponse {\n");
    
    sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
    sb.append("    kpIs: ").append(toIndentedString(kpis)).append("\n");
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

