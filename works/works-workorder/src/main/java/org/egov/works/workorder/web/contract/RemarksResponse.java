package org.egov.works.workorder.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Contract class to send response. Array of Remarks items are used in case of search results, also multiple Remarks item is used for create and update
 */
@ApiModel(description = "Contract class to send response. Array of Remarks items are used in case of search results, also multiple Remarks item is used for create and update")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-01-02T12:11:15.167Z")

public class RemarksResponse   {
  @JsonProperty("ResponseInfo")
  private ResponseInfo responseInfo = null;

  @JsonProperty("remarks")
  private List<Remarks> remarks = null;

  public RemarksResponse responseInfo(ResponseInfo responseInfo) {
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

  public RemarksResponse remarks(List<Remarks> remarks) {
    this.remarks = remarks;
    return this;
  }

  public RemarksResponse addRemarksItem(Remarks remarksItem) {
    if (this.remarks == null) {
      this.remarks = new ArrayList<Remarks>();
    }
    this.remarks.add(remarksItem);
    return this;
  }

   /**
   * Used for search result and create only
   * @return remarks
  **/
  @ApiModelProperty(value = "Used for search result and create only")

  @Valid

  public List<Remarks> getRemarks() {
    return remarks;
  }

  public void setRemarks(List<Remarks> remarks) {
    this.remarks = remarks;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RemarksResponse remarksResponse = (RemarksResponse) o;
    return Objects.equals(this.responseInfo, remarksResponse.responseInfo) &&
        Objects.equals(this.remarks, remarksResponse.remarks);
  }

  @Override
  public int hashCode() {
    return Objects.hash(responseInfo, remarks);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RemarksResponse {\n");
    
    sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
    sb.append("    remarks: ").append(toIndentedString(remarks)).append("\n");
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

