package org.egov.works.estimate.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Contract class to send response. Array of Project Code items are used in case of search results, also multiple Project Code item is used for create and update
 */
@ApiModel(description = "Contract class to send response. Array of Project Code items are used in case of search results, also multiple Project Code item is used for create and update")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-29T09:03:53.949Z")

public class ProjectCodeResponse   {
  @JsonProperty("ResponseInfo")
  private ResponseInfo responseInfo = null;

  @JsonProperty("projectCodes")
  private List<ProjectCode> projectCodes = null;

  public ProjectCodeResponse responseInfo(ResponseInfo responseInfo) {
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

  public ProjectCodeResponse projectCodes(List<ProjectCode> projectCodes) {
    this.projectCodes = projectCodes;
    return this;
  }

  public ProjectCodeResponse addProjectCodesItem(ProjectCode projectCodesItem) {
    if (this.projectCodes == null) {
      this.projectCodes = new ArrayList<ProjectCode>();
    }
    this.projectCodes.add(projectCodesItem);
    return this;
  }

   /**
   * Used for search result and create only
   * @return projectCodes
  **/
  @ApiModelProperty(value = "Used for search result and create only")

  @Valid

  public List<ProjectCode> getProjectCodes() {
    return projectCodes;
  }

  public void setProjectCodes(List<ProjectCode> projectCodes) {
    this.projectCodes = projectCodes;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProjectCodeResponse projectCodeResponse = (ProjectCodeResponse) o;
    return Objects.equals(this.responseInfo, projectCodeResponse.responseInfo) &&
        Objects.equals(this.projectCodes, projectCodeResponse.projectCodes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(responseInfo, projectCodes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProjectCodeResponse {\n");
    
    sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
    sb.append("    projectCodes: ").append(toIndentedString(projectCodes)).append("\n");
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

