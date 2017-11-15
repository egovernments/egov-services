package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.MaterialIssue;
import io.swagger.model.RequestInfo;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Contract class for web request. Array of MaterialIssue items  are used in case of create or update
 */
@ApiModel(description = "Contract class for web request. Array of MaterialIssue items  are used in case of create or update")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-28T13:21:55.964+05:30")

public class MaterialIssueRequest   {
  @JsonProperty("requestInfo")
  private RequestInfo requestInfo = null;

  @JsonProperty("materialIssues")
  @Valid
  private List<MaterialIssue> materialIssues = null;

  public MaterialIssueRequest requestInfo(RequestInfo requestInfo) {
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

  public MaterialIssueRequest materialIssues(List<MaterialIssue> materialIssues) {
    this.materialIssues = materialIssues;
    return this;
  }

  public MaterialIssueRequest addMaterialIssuesItem(MaterialIssue materialIssuesItem) {
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MaterialIssueRequest materialIssueRequest = (MaterialIssueRequest) o;
    return Objects.equals(this.requestInfo, materialIssueRequest.requestInfo) &&
        Objects.equals(this.materialIssues, materialIssueRequest.materialIssues);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestInfo, materialIssues);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MaterialIssueRequest {\n");
    
    sb.append("    requestInfo: ").append(toIndentedString(requestInfo)).append("\n");
    sb.append("    materialIssues: ").append(toIndentedString(materialIssues)).append("\n");
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

