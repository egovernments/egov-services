package org.egov.pa.web.contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.egov.common.contract.request.RequestInfo;
import org.egov.pa.model.KPI;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * KPIRequest
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-25T11:25:20.008Z")

public class KPIRequest   {
  @JsonProperty("RequestInfo")
  private RequestInfo requestInfo = null;

  @JsonProperty("KPIs")
  private List<org.egov.pa.model.KPI> kpis = new ArrayList<KPI>();

  public KPIRequest requestInfo(RequestInfo requestInfo) {
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

  public KPIRequest kpis(List<KPI> kpIs) {
    this.kpis = kpIs;
    return this;
  }

  public KPIRequest addKpisItem(KPI kpIsItem) {
    this.kpis.add(kpIsItem);
    return this;
  }

   /**
   * Get kpIs
   * @return kpIs
  **/
  @NotNull

  @Valid

  public List<KPI> getKpis() {
    return kpis;
  }

  public void setKpis(List<KPI> kpIs) {
    this.kpis = kpIs;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    KPIRequest kpIRequest = (KPIRequest) o;
    return Objects.equals(this.requestInfo, kpIRequest.requestInfo) &&
        Objects.equals(this.kpis, kpIRequest.kpis);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestInfo, kpis);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class KPIRequest {\n");
    
    sb.append("    requestInfo: ").append(toIndentedString(requestInfo)).append("\n");
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

