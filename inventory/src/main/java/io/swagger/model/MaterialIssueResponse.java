package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.MaterialIssue;
import io.swagger.model.Page;
import io.swagger.model.ResponseInfo;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Contract class for web response. Array of MaterialIssue items  are used in case of search ,create or update request.
 */
@ApiModel(description = "Contract class for web response. Array of MaterialIssue items  are used in case of search ,create or update request.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-28T13:21:55.964+05:30")

public class MaterialIssueResponse   {
  @JsonProperty("responseInfo")
  private ResponseInfo responseInfo = null;

  @JsonProperty("materialIssues")
  @Valid
  private List<MaterialIssue> materialIssues = null;

  @JsonProperty("page")
  private Page page = null;

  public MaterialIssueResponse responseInfo(ResponseInfo responseInfo) {
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

  public MaterialIssueResponse materialIssues(List<MaterialIssue> materialIssues) {
    this.materialIssues = materialIssues;
    return this;
  }

  public MaterialIssueResponse addMaterialIssuesItem(MaterialIssue materialIssuesItem) {
    if (this.materialIssues == null) {
      this.materialIssues = new ArrayList<MaterialIssue>();
    }
    this.materialIssues.add(materialIssuesItem);
    return this;
  }

   /**
   * Used for search result and create only
   * @return materialIssues
  **/
  @ApiModelProperty(value = "Used for search result and create only")

  @Valid

  public List<MaterialIssue> getMaterialIssues() {
    return materialIssues;
  }

  public void setMaterialIssues(List<MaterialIssue> materialIssues) {
    this.materialIssues = materialIssues;
  }

  public MaterialIssueResponse page(Page page) {
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
    MaterialIssueResponse materialIssueResponse = (MaterialIssueResponse) o;
    return Objects.equals(this.responseInfo, materialIssueResponse.responseInfo) &&
        Objects.equals(this.materialIssues, materialIssueResponse.materialIssues) &&
        Objects.equals(this.page, materialIssueResponse.page);
  }

  @Override
  public int hashCode() {
    return Objects.hash(responseInfo, materialIssues, page);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MaterialIssueResponse {\n");
    
    sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
    sb.append("    materialIssues: ").append(toIndentedString(materialIssues)).append("\n");
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

