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
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-29T09:03:53.949Z")

public class RevisionEstimateRequest   {
  @JsonProperty("RequestInfo")
  private RequestInfo requestInfo = null;

  @JsonProperty("revisionEstimates")
  private List<RevisionEstimate> revisionEstimates = null;

  public RevisionEstimateRequest requestInfo(RequestInfo requestInfo) {
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

  public RevisionEstimateRequest revisionEstimates(List<RevisionEstimate> revisionEstimates) {
    this.revisionEstimates = revisionEstimates;
    return this;
  }

  public RevisionEstimateRequest addRevisionEstimatesItem(RevisionEstimate revisionEstimatesItem) {
    if (this.revisionEstimates == null) {
      this.revisionEstimates = new ArrayList<RevisionEstimate>();
    }
    this.revisionEstimates.add(revisionEstimatesItem);
    return this;
  }

   /**
   * Used for create and update only
   * @return revisionEstimates
  **/
  @ApiModelProperty(value = "Used for create and update only")

  @Valid

  public List<RevisionEstimate> getRevisionEstimates() {
    return revisionEstimates;
  }

  public void setRevisionEstimates(List<RevisionEstimate> revisionEstimates) {
    this.revisionEstimates = revisionEstimates;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RevisionEstimateRequest revisionEstimateRequest = (RevisionEstimateRequest) o;
    return Objects.equals(this.requestInfo, revisionEstimateRequest.requestInfo) &&
        Objects.equals(this.revisionEstimates, revisionEstimateRequest.revisionEstimates);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestInfo, revisionEstimates);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RevisionEstimateRequest {\n");
    
    sb.append("    requestInfo: ").append(toIndentedString(requestInfo)).append("\n");
    sb.append("    revisionEstimates: ").append(toIndentedString(revisionEstimates)).append("\n");
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

