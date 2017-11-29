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

public class AbstractEstimateResponse   {
  @JsonProperty("ResponseInfo")
  private ResponseInfo responseInfo = null;

  @JsonProperty("abstractEstimates")
  private List<AbstractEstimate> abstractEstimates = null;

  public AbstractEstimateResponse responseInfo(ResponseInfo responseInfo) {
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

  public AbstractEstimateResponse abstractEstimates(List<AbstractEstimate> abstractEstimates) {
    this.abstractEstimates = abstractEstimates;
    return this;
  }

  public AbstractEstimateResponse addAbstractEstimatesItem(AbstractEstimate abstractEstimatesItem) {
    if (this.abstractEstimates == null) {
      this.abstractEstimates = new ArrayList<AbstractEstimate>();
    }
    this.abstractEstimates.add(abstractEstimatesItem);
    return this;
  }

   /**
   * Used for search result and create only
   * @return abstractEstimates
  **/
  @ApiModelProperty(value = "Used for search result and create only")

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
    AbstractEstimateResponse abstractEstimateResponse = (AbstractEstimateResponse) o;
    return Objects.equals(this.responseInfo, abstractEstimateResponse.responseInfo) &&
        Objects.equals(this.abstractEstimates, abstractEstimateResponse.abstractEstimates);
  }

  @Override
  public int hashCode() {
    return Objects.hash(responseInfo, abstractEstimates);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AbstractEstimateResponse {\n");
    
    sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
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

