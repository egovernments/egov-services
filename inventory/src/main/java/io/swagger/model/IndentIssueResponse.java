package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.IndentIssue;
import io.swagger.model.Page;
import io.swagger.model.ResponseInfo;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Contract class for web response. Array of IndentIssue items  are used in case of search ,create or update request.
 */
@ApiModel(description = "Contract class for web response. Array of IndentIssue items  are used in case of search ,create or update request.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-28T13:21:55.964+05:30")

public class IndentIssueResponse   {
  @JsonProperty("responseInfo")
  private ResponseInfo responseInfo = null;

  @JsonProperty("indentIssues")
  @Valid
  private List<IndentIssue> indentIssues = null;

  @JsonProperty("page")
  private Page page = null;

  public IndentIssueResponse responseInfo(ResponseInfo responseInfo) {
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

  public IndentIssueResponse indentIssues(List<IndentIssue> indentIssues) {
    this.indentIssues = indentIssues;
    return this;
  }

  public IndentIssueResponse addIndentIssuesItem(IndentIssue indentIssuesItem) {
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

  public IndentIssueResponse page(Page page) {
    this.page = page;
    return this;
  }

   /**
   * Get page
   * @return page
  **/
  @ApiModelProperty(value = "")

  @Valid

  public Page getPage() {
    return page;
  }

  public void setPage(Page page) {
    this.page = page;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IndentIssueResponse indentIssueResponse = (IndentIssueResponse) o;
    return Objects.equals(this.responseInfo, indentIssueResponse.responseInfo) &&
        Objects.equals(this.indentIssues, indentIssueResponse.indentIssues) &&
        Objects.equals(this.page, indentIssueResponse.page);
  }

  @Override
  public int hashCode() {
    return Objects.hash(responseInfo, indentIssues, page);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IndentIssueResponse {\n");
    
    sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
    sb.append("    indentIssues: ").append(toIndentedString(indentIssues)).append("\n");
    sb.append("    page: ").append(toIndentedString(page)).append("\n");
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

