package org.egov.works.masters.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Contract class to send response. Array of Estimate Template items are used in case of search results, also multiple Estimate Template item is used for create and update
 */
@ApiModel(description = "Contract class to send response. Array of Estimate Template items are used in case of search results, also multiple Estimate Template item is used for create and update")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-09T12:57:08.229Z")

public class EstimateTemplateRequest   {
  @JsonProperty("RequestInfo")
  private RequestInfo requestInfo = null;

  @JsonProperty("estimateTemplates")
  private List<EstimateTemplate> estimateTemplates = null;

  public EstimateTemplateRequest requestInfo(RequestInfo requestInfo) {
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

  public EstimateTemplateRequest estimateTemplates(List<EstimateTemplate> estimateTemplates) {
    this.estimateTemplates = estimateTemplates;
    return this;
  }

  public EstimateTemplateRequest addEstimateTemplatesItem(EstimateTemplate estimateTemplatesItem) {
    if (this.estimateTemplates == null) {
      this.estimateTemplates = new ArrayList<EstimateTemplate>();
    }
    this.estimateTemplates.add(estimateTemplatesItem);
    return this;
  }

   /**
   * Used for create and update only
   * @return estimateTemplates
  **/
  @ApiModelProperty(value = "Used for create and update only")

  @Valid

  public List<EstimateTemplate> getEstimateTemplates() {
    return estimateTemplates;
  }

  public void setEstimateTemplates(List<EstimateTemplate> estimateTemplates) {
    this.estimateTemplates = estimateTemplates;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EstimateTemplateRequest estimateTemplateRequest = (EstimateTemplateRequest) o;
    return Objects.equals(this.requestInfo, estimateTemplateRequest.requestInfo) &&
        Objects.equals(this.estimateTemplates, estimateTemplateRequest.estimateTemplates);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestInfo, estimateTemplates);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EstimateTemplateRequest {\n");
    
    sb.append("    requestInfo: ").append(toIndentedString(requestInfo)).append("\n");
    sb.append("    estimateTemplates: ").append(toIndentedString(estimateTemplates)).append("\n");
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

