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

public class EstimateTemplateResponse   {
  @JsonProperty("ResponseInfo")
  private ResponseInfo responseInfo = null;

  @JsonProperty("estimateTemplates")
  private List<EstimateTemplate> estimateTemplates = null;

  public EstimateTemplateResponse responseInfo(ResponseInfo responseInfo) {
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

  public EstimateTemplateResponse estimateTemplates(List<EstimateTemplate> estimateTemplates) {
    this.estimateTemplates = estimateTemplates;
    return this;
  }

  public EstimateTemplateResponse addEstimateTemplatesItem(EstimateTemplate estimateTemplatesItem) {
    if (this.estimateTemplates == null) {
      this.estimateTemplates = new ArrayList<EstimateTemplate>();
    }
    this.estimateTemplates.add(estimateTemplatesItem);
    return this;
  }

   /**
   * Used for search result and create only
   * @return estimateTemplates
  **/
  @ApiModelProperty(value = "Used for search result and create only")

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
    EstimateTemplateResponse estimateTemplateResponse = (EstimateTemplateResponse) o;
    return Objects.equals(this.responseInfo, estimateTemplateResponse.responseInfo) &&
        Objects.equals(this.estimateTemplates, estimateTemplateResponse.estimateTemplates);
  }

  @Override
  public int hashCode() {
    return Objects.hash(responseInfo, estimateTemplates);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EstimateTemplateResponse {\n");
    
    sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
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

