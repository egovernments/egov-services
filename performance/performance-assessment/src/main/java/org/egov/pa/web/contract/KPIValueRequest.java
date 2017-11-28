package org.egov.pa.web.contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.egov.common.contract.request.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * KPIValueRequest
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-02T05:16:16.756Z")

public class KPIValueRequest   {
  @JsonProperty("RequestInfo")
  private RequestInfo requestInfo = null;

  @JsonProperty("kpiValues")
  private List<ValueResponse> kpiValues = new ArrayList<ValueResponse>();

  public KPIValueRequest requestInfo(RequestInfo requestInfo) {
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

  public KPIValueRequest kpiValues(List<ValueResponse> kpiValues) {
    this.kpiValues = kpiValues;
    return this;
  }

  public KPIValueRequest addKpiValuesItem(ValueResponse kpiValuesItem) {
    this.kpiValues.add(kpiValuesItem);
    return this;
  }

   /**
   * Get kpiValues
   * @return kpiValues
  **/
  @NotNull

  @Valid

  public List<ValueResponse> getKpiValues() {
    return kpiValues;
  }

  public void setKpiValues(List<ValueResponse> kpiValues) {
    this.kpiValues = kpiValues;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    KPIValueRequest kpIValueRequest = (KPIValueRequest) o;
    return Objects.equals(this.requestInfo, kpIValueRequest.requestInfo) &&
        Objects.equals(this.kpiValues, kpIValueRequest.kpiValues);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestInfo, kpiValues);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class KPIValueRequest {\n");
    
    sb.append("    requestInfo: ").append(toIndentedString(requestInfo)).append("\n");
    sb.append("    kpiValues: ").append(toIndentedString(kpiValues)).append("\n");
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

