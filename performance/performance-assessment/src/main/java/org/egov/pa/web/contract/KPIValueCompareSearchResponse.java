package org.egov.pa.web.contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.pa.model.ULBKpiValueList;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * KPIValueSearchResponse
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-02T05:16:16.756Z")

public class KPIValueCompareSearchResponse   {
  @JsonProperty("responseInfo")
  private ResponseInfo responseInfo = null;

  @JsonProperty("ulbs")
  private List<ULBKpiValueList> ulbKpiValues = null;

  public KPIValueCompareSearchResponse responseInfo(ResponseInfo responseInfo) {
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

  public KPIValueCompareSearchResponse kpiValues(List<ULBKpiValueList> kpiValues) {
    this.ulbKpiValues = kpiValues;
    return this;
  }

  public KPIValueCompareSearchResponse addKpiValuesItem(ULBKpiValueList kpiValuesItem) {
    if (this.ulbKpiValues == null) {
      this.ulbKpiValues = new ArrayList<ULBKpiValueList>();
    }
    this.ulbKpiValues.add(kpiValuesItem);
    return this;
  }

   /**
   * Get kpiValues
   * @return kpiValues
  **/

  @Valid
  


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    KPIValueCompareSearchResponse kpIValueSearchResponse = (KPIValueCompareSearchResponse) o;
    return Objects.equals(this.responseInfo, kpIValueSearchResponse.responseInfo) &&
        Objects.equals(this.ulbKpiValues, kpIValueSearchResponse.ulbKpiValues);
  }

  public List<ULBKpiValueList> getUlbKpiValues() {
	return ulbKpiValues;
}

public void setUlbKpiValues(List<ULBKpiValueList> ulbKpiValues) {
	this.ulbKpiValues = ulbKpiValues;
}

@Override
  public int hashCode() {
    return Objects.hash(responseInfo, ulbKpiValues);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class KPIValueSearchResponse {\n");
    
    sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
    sb.append("    kpiValues: ").append(toIndentedString(ulbKpiValues)).append("\n");
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

