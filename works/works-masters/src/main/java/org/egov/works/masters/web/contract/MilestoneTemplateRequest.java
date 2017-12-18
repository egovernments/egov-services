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

public class MilestoneTemplateRequest   {
  @JsonProperty("RequestInfo")
  private RequestInfo requestInfo = null;

  @JsonProperty("milestoneTemplates")
  private List<MilestoneTemplate> milestoneTemplates = null;

  public MilestoneTemplateRequest requestInfo(RequestInfo requestInfo) {
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

  public MilestoneTemplateRequest milestoneTemplates(List<MilestoneTemplate> milestoneTemplates) {
    this.milestoneTemplates = milestoneTemplates;
    return this;
  }

  public MilestoneTemplateRequest addMilestoneTemplatesItem(MilestoneTemplate milestoneTemplatesItem) {
    if (this.milestoneTemplates == null) {
      this.milestoneTemplates = new ArrayList<MilestoneTemplate>();
    }
    this.milestoneTemplates.add(milestoneTemplatesItem);
    return this;
  }

   /**
   * Used for create and update only
   * @return milestoneTemplates
  **/
  @ApiModelProperty(value = "Used for create and update only")

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
    MilestoneTemplateRequest milestoneTemplateRequest = (MilestoneTemplateRequest) o;
    return Objects.equals(this.requestInfo, milestoneTemplateRequest.requestInfo) &&
        Objects.equals(this.milestoneTemplates, milestoneTemplateRequest.milestoneTemplates);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestInfo, milestoneTemplates);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MilestoneTemplateRequest {\n");
    
    sb.append("    requestInfo: ").append(toIndentedString(requestInfo)).append("\n");
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

