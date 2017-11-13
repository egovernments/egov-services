package org.egov.works.estimate.web.contract;

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
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-02T07:23:18.632Z")

public class ScheduleOfRateRequest   {
  @JsonProperty("requestInfo")
  private RequestInfo requestInfo = null;

  @JsonProperty("scheduleOfRates")
  private List<ScheduleOfRate> scheduleOfRates = null;

  public ScheduleOfRateRequest requestInfo(RequestInfo requestInfo) {
    this.requestInfo = requestInfo;
    return this;
  }

   /**
   * Get requestInfo
   * @return requestInfo
  **/
  @ApiModelProperty(value = "")

  @Valid

  public RequestInfo getRequestInfo() {
    return requestInfo;
  }

  public void setRequestInfo(RequestInfo requestInfo) {
    this.requestInfo = requestInfo;
  }

  public ScheduleOfRateRequest scheduleOfRates(List<ScheduleOfRate> scheduleOfRates) {
    this.scheduleOfRates = scheduleOfRates;
    return this;
  }

  public ScheduleOfRateRequest addScheduleOfRatesItem(ScheduleOfRate scheduleOfRatesItem) {
    if (this.scheduleOfRates == null) {
      this.scheduleOfRates = new ArrayList<ScheduleOfRate>();
    }
    this.scheduleOfRates.add(scheduleOfRatesItem);
    return this;
  }

   /**
   * Used for create and update only
   * @return scheduleOfRates
  **/
  @ApiModelProperty(value = "Used for create and update only")

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
    ScheduleOfRateRequest scheduleOfRateRequest = (ScheduleOfRateRequest) o;
    return Objects.equals(this.requestInfo, scheduleOfRateRequest.requestInfo) &&
        Objects.equals(this.scheduleOfRates, scheduleOfRateRequest.scheduleOfRates);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestInfo, scheduleOfRates);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ScheduleOfRateRequest {\n");
    
    sb.append("    requestInfo: ").append(toIndentedString(requestInfo)).append("\n");
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

