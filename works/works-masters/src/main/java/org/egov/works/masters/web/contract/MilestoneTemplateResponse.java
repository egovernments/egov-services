package org.egov.works.masters.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Contract class to send response. Array of Milestone Template items are used in case of search results, also multiple Milestone Template item is used for create and update
 */
@ApiModel(description = "Contract class to send response. Array of Milestone Template items are used in case of search results, also multiple Milestone Template item is used for create and update")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-12-15T12:28:51.418Z")

public class MilestoneTemplateResponse   {
  @JsonProperty("ResponseInfo")
  private ResponseInfo responseInfo = null;

  @JsonProperty("milestoneTemplates")
  private List<MilestoneTemplate> milestoneTemplates = null;

  public MilestoneTemplateResponse responseInfo(ResponseInfo responseInfo) {
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

  public MilestoneTemplateResponse milestoneTemplates(List<MilestoneTemplate> milestoneTemplates) {
    this.milestoneTemplates = milestoneTemplates;
    return this;
  }

  public MilestoneTemplateResponse addMilestoneTemplatesItem(MilestoneTemplate milestoneTemplatesItem) {
    if (this.milestoneTemplates == null) {
      this.milestoneTemplates = new ArrayList<MilestoneTemplate>();
    }
    this.milestoneTemplates.add(milestoneTemplatesItem);
    return this;
  }

   /**
   * Used for search result and create only
   * @return milestoneTemplates
  **/
  @ApiModelProperty(value = "Used for search result and create only")

  @Valid

  public List<MilestoneTemplate> getMilestoneTemplates() {
    return milestoneTemplates;
  }

  public void setMilestoneTemplates(List<MilestoneTemplate> milestoneTemplates) {
    this.milestoneTemplates = milestoneTemplates;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MilestoneTemplateResponse milestoneTemplateResponse = (MilestoneTemplateResponse) o;
    return Objects.equals(this.responseInfo, milestoneTemplateResponse.responseInfo) &&
        Objects.equals(this.milestoneTemplates, milestoneTemplateResponse.milestoneTemplates);
  }

  @Override
  public int hashCode() {
    return Objects.hash(responseInfo, milestoneTemplates);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MilestoneTemplateResponse {\n");
    
    sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
    sb.append("    milestoneTemplates: ").append(toIndentedString(milestoneTemplates)).append("\n");
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

