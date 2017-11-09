package org.egov.inv.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.egov.inv.model.MaterialIssue;
import org.egov.inv.model.RequestInfo;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Contract class for web request. Array of MaterialIssue items  are used in case of create or update
 */
@ApiModel(description = "Contract class for web request. Array of MaterialIssue items  are used in case of create or update")
@javax.annotation.Generated(value = "org.egov.inv.codegen.languages.SpringCodegen", date = "2017-11-08T13:51:07.770Z")

public class MaterialIssueRequest   {
  @JsonProperty("RequestInfo")
  private RequestInfo requestInfo = null;

  @JsonProperty("materialIssues")
  private List<MaterialIssue> materialIssues = new ArrayList<MaterialIssue>();

  public MaterialIssueRequest requestInfo(RequestInfo requestInfo) {
    this.requestInfo = requestInfo;
    return this;
  }

   /**
   * Get requestInfo
   * @return requestInfo
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

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
    this.materialIssues.add(materialIssuesItem);
    return this;
  }

   /**
   * Used for search result and create only
   * @return materialIssues
  **/
  @ApiModelProperty(required = true, value = "Used for search result and create only")
  @NotNull

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

