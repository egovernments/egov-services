package org.egov.works.estimate.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Contract class to send response. Array of AbstractEstimate items are used in case of search results, also multiple  AbstractEstimate item is used for create and update
 */
@ApiModel(description = "Contract class to send response. Array of AbstractEstimate items are used in case of search results, also multiple  AbstractEstimate item is used for create and update")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-29T09:03:53.949Z")

public class AbstractEstimateRequest   {
  @JsonProperty("RequestInfo")
  private RequestInfo requestInfo = null;

  @JsonProperty("abstractEstimates")
  private List<AbstractEstimate> abstractEstimates = null;

  public AbstractEstimateRequest requestInfo(RequestInfo requestInfo) {
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

  public AbstractEstimateRequest abstractEstimates(List<AbstractEstimate> abstractEstimates) {
    this.abstractEstimates = abstractEstimates;
    return this;
  }

  public AbstractEstimateRequest addAbstractEstimatesItem(AbstractEstimate abstractEstimatesItem) {
    if (this.abstractEstimates == null) {
      this.abstractEstimates = new ArrayList<AbstractEstimate>();
    }
    this.abstractEstimates.add(abstractEstimatesItem);
    return this;
  }

   /**
   * Used for create and update only
   * @return abstractEstimates
  **/
  @ApiModelProperty(value = "Used for create and update only")

  @Valid

  public List<AbstractEstimate> getAbstractEstimates() {
    return abstractEstimates;
  }

  public void setAbstractEstimates(List<AbstractEstimate> abstractEstimates) {
    this.abstractEstimates = abstractEstimates;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AbstractEstimateRequest abstractEstimateRequest = (AbstractEstimateRequest) o;
    return Objects.equals(this.requestInfo, abstractEstimateRequest.requestInfo) &&
        Objects.equals(this.abstractEstimates, abstractEstimateRequest.abstractEstimates);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestInfo, abstractEstimates);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AbstractEstimateRequest {\n");
    
    sb.append("    requestInfo: ").append(toIndentedString(requestInfo)).append("\n");
    sb.append("    abstractEstimates: ").append(toIndentedString(abstractEstimates)).append("\n");
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

