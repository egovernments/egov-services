package org.egov.works.estimate.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Contract class to send response. Array of Detailed Estimate items are used in case of search results, also multiple  Detailed Estimate item is used for create and update
 */
@ApiModel(description = "Contract class to send response. Array of Detailed Estimate items are used in case of search results, also multiple  Detailed Estimate item is used for create and update")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-29T09:03:53.949Z")

public class DetailedEstimateRequest   {
  @JsonProperty("RequestInfo")
  private RequestInfo requestInfo = null;

  @JsonProperty("detailedEstimates")
  private List<DetailedEstimate> detailedEstimates = null;

  public DetailedEstimateRequest requestInfo(RequestInfo requestInfo) {
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

  public DetailedEstimateRequest detailedEstimates(List<DetailedEstimate> detailedEstimates) {
    this.detailedEstimates = detailedEstimates;
    return this;
  }

  public DetailedEstimateRequest addDetailedEstimatesItem(DetailedEstimate detailedEstimatesItem) {
    if (this.detailedEstimates == null) {
      this.detailedEstimates = new ArrayList<DetailedEstimate>();
    }
    this.detailedEstimates.add(detailedEstimatesItem);
    return this;
  }

   /**
   * Used for create and update only
   * @return detailedEstimates
  **/
  @ApiModelProperty(value = "Used for create and update only")

  @Valid

  public List<DetailedEstimate> getDetailedEstimates() {
    return detailedEstimates;
  }

  public void setDetailedEstimates(List<DetailedEstimate> detailedEstimates) {
    this.detailedEstimates = detailedEstimates;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DetailedEstimateRequest detailedEstimateRequest = (DetailedEstimateRequest) o;
    return Objects.equals(this.requestInfo, detailedEstimateRequest.requestInfo) &&
        Objects.equals(this.detailedEstimates, detailedEstimateRequest.detailedEstimates);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestInfo, detailedEstimates);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DetailedEstimateRequest {\n");
    
    sb.append("    requestInfo: ").append(toIndentedString(requestInfo)).append("\n");
    sb.append("    detailedEstimates: ").append(toIndentedString(detailedEstimates)).append("\n");
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

