package org.egov.works.masters.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Contract class to send response. Array of Revision Estimate items are used in case of search results, also multiple  Revision Estimate item is used for create and update
 */
@ApiModel(description = "Contract class to send response. Array of Revision Estimate items are used in case of search results, also multiple  Revision Estimate item is used for create and update")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-09T12:57:08.229Z")

public class ScheduleOfRateResponse   {
  @JsonProperty("ResponseInfo")
  private ResponseInfo responseInfo = null;

  @JsonProperty("scheduleOfRates")
  private List<ScheduleOfRate> scheduleOfRates = null;

  public ScheduleOfRateResponse responseInfo(ResponseInfo responseInfo) {
    this.responseInfo = responseInfo;
    return this;
  }

   /**
   * Get responseInfo
   * @return responseInfo
  **/
  @ApiModelProperty(value = "")

  @Valid

  public ResponseInfo getResponseInfo() {
    return responseInfo;
  }

  public void setResponseInfo(ResponseInfo responseInfo) {
    this.responseInfo = responseInfo;
  }

  public ScheduleOfRateResponse scheduleOfRates(List<ScheduleOfRate> scheduleOfRates) {
    this.scheduleOfRates = scheduleOfRates;
    return this;
  }

  public ScheduleOfRateResponse addScheduleOfRatesItem(ScheduleOfRate scheduleOfRatesItem) {
    if (this.scheduleOfRates == null) {
      this.scheduleOfRates = new ArrayList<ScheduleOfRate>();
    }
    this.scheduleOfRates.add(scheduleOfRatesItem);
    return this;
  }

   /**
   * Used for search result and create only
   * @return scheduleOfRates
  **/
  @ApiModelProperty(value = "Used for search result and create only")

  @Valid

  public List<ScheduleOfRate> getScheduleOfRates() {
    return scheduleOfRates;
  }

  public void setScheduleOfRates(List<ScheduleOfRate> scheduleOfRates) {
    this.scheduleOfRates = scheduleOfRates;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ScheduleOfRateResponse scheduleOfRateResponse = (ScheduleOfRateResponse) o;
    return Objects.equals(this.responseInfo, scheduleOfRateResponse.responseInfo) &&
        Objects.equals(this.scheduleOfRates, scheduleOfRateResponse.scheduleOfRates);
  }

  @Override
  public int hashCode() {
    return Objects.hash(responseInfo, scheduleOfRates);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ScheduleOfRateResponse {\n");
    
    sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
    sb.append("    scheduleOfRates: ").append(toIndentedString(scheduleOfRates)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

