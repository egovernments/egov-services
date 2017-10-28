package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.IndentIssue;
import io.swagger.model.RequestInfo;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Contract class for web request. Array of IndentIssue items  are used in case of create or update
 */
@ApiModel(description = "Contract class for web request. Array of IndentIssue items  are used in case of create or update")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-28T13:21:55.964+05:30")

public class IndentIssueRequest   {
  @JsonProperty("requestInfo")
  private RequestInfo requestInfo = null;

  @JsonProperty("indentIssues")
  @Valid
  private List<IndentIssue> indentIssues = null;

  public IndentIssueRequest requestInfo(RequestInfo requestInfo) {
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

  public IndentIssueRequest indentIssues(List<IndentIssue> indentIssues) {
    this.indentIssues = indentIssues;
    return this;
  }

  public IndentIssueRequest addIndentIssuesItem(IndentIssue indentIssuesItem) {
    if (this.indentIssues == null) {
      this.indentIssues = new ArrayList<IndentIssue>();
    }
    this.indentIssues.add(indentIssuesItem);
    return this;
  }

   /**
   * Used for search result and create only
   * @return indentIssues
  **/
  @ApiModelProperty(value = "Used for search result and create only")

  @Valid

  public List<IndentIssue> getIndentIssues() {
    return indentIssues;
  }

  public void setIndentIssues(List<IndentIssue> indentIssues) {
    this.indentIssues = indentIssues;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IndentIssueRequest indentIssueRequest = (IndentIssueRequest) o;
    return Objects.equals(this.requestInfo, indentIssueRequest.requestInfo) &&
        Objects.equals(this.indentIssues, indentIssueRequest.indentIssues);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestInfo, indentIssues);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IndentIssueRequest {\n");
    
    sb.append("    requestInfo: ").append(toIndentedString(requestInfo)).append("\n");
    sb.append("    indentIssues: ").append(toIndentedString(indentIssues)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

