package org.egov.pa.web.contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.pa.model.KpiValueList;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * KPIValueSearchResponse
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-25T11:25:20.008Z")

public class KPIValueSearchResponse   {
  @JsonProperty("responseInfo")
  private ResponseInfo responseInfo = null;

  @JsonProperty("kpiValuesList")
  private List<KpiValueList> kpiValuesList = null;

  public KPIValueSearchResponse responseInfo(ResponseInfo responseInfo) {
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

  public KPIValueSearchResponse kpiValuesList(List<KpiValueList> kpiValuesList) {
    this.kpiValuesList = kpiValuesList;
    return this;
  }

  public KPIValueSearchResponse addKpiValuesListItem(KpiValueList kpiValuesListItem) {
    if (this.kpiValuesList == null) {
      this.kpiValuesList = new ArrayList<KpiValueList>();
    }
    this.kpiValuesList.add(kpiValuesListItem);
    return this;
  }

   /**
   * Get kpiValuesList
   * @return kpiValuesList
  **/

  @Valid

  public List<KpiValueList> getKpiValuesList() {
    return kpiValuesList;
  }

  public void setKpiValuesList(List<KpiValueList> kpiValuesList) {
    this.kpiValuesList = kpiValuesList;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    KPIValueSearchResponse kpIValueSearchResponse = (KPIValueSearchResponse) o;
    return Objects.equals(this.responseInfo, kpIValueSearchResponse.responseInfo) &&
        Objects.equals(this.kpiValuesList, kpIValueSearchResponse.kpiValuesList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(responseInfo, kpiValuesList);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class KPIValueSearchResponse {\n");
    
    sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
    sb.append("    kpiValuesList: ").append(toIndentedString(kpiValuesList)).append("\n");
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

