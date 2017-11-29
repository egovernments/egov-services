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

public class RevisionEstimateResponse   {
  @JsonProperty("ResponseInfo")
  private ResponseInfo responseInfo = null;

  @JsonProperty("RevisiontEstimates")
  private List<RevisionEstimate> revisiontEstimates = null;

  public RevisionEstimateResponse responseInfo(ResponseInfo responseInfo) {
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

  public RevisionEstimateResponse revisiontEstimates(List<RevisionEstimate> revisiontEstimates) {
    this.revisiontEstimates = revisiontEstimates;
    return this;
  }

  public RevisionEstimateResponse addRevisiontEstimatesItem(RevisionEstimate revisiontEstimatesItem) {
    if (this.revisiontEstimates == null) {
      this.revisiontEstimates = new ArrayList<RevisionEstimate>();
    }
    this.revisiontEstimates.add(revisiontEstimatesItem);
    return this;
  }

   /**
   * Used for search result and create only
   * @return revisiontEstimates
  **/
  @ApiModelProperty(value = "Used for search result and create only")

  @Valid

  public List<RevisionEstimate> getRevisiontEstimates() {
    return revisiontEstimates;
  }

  public void setRevisiontEstimates(List<RevisionEstimate> revisiontEstimates) {
    this.revisiontEstimates = revisiontEstimates;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RevisionEstimateResponse revisionEstimateResponse = (RevisionEstimateResponse) o;
    return Objects.equals(this.responseInfo, revisionEstimateResponse.responseInfo) &&
        Objects.equals(this.revisiontEstimates, revisionEstimateResponse.revisiontEstimates);
  }

  @Override
  public int hashCode() {
    return Objects.hash(responseInfo, revisiontEstimates);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RevisionEstimateResponse {\n");
    
    sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
    sb.append("    revisiontEstimates: ").append(toIndentedString(revisiontEstimates)).append("\n");
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

