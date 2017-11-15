package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.Page;
import io.swagger.model.ResponseInfo;
import io.swagger.model.TransferOutward;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Contract class for web response. Array of TransferOutward items  are used in case of search ,create or update request.
 */
@ApiModel(description = "Contract class for web response. Array of TransferOutward items  are used in case of search ,create or update request.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-28T13:21:55.964+05:30")

public class TransferOutwardResponse   {
  @JsonProperty("responseInfo")
  private ResponseInfo responseInfo = null;

  @JsonProperty("transferOutwards")
  @Valid
  private List<TransferOutward> transferOutwards = null;

  @JsonProperty("page")
  private Page page = null;

  public TransferOutwardResponse responseInfo(ResponseInfo responseInfo) {
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

  public TransferOutwardResponse transferOutwards(List<TransferOutward> transferOutwards) {
    this.transferOutwards = transferOutwards;
    return this;
  }

  public TransferOutwardResponse addTransferOutwardsItem(TransferOutward transferOutwardsItem) {
    if (this.transferOutwards == null) {
      this.transferOutwards = new ArrayList<TransferOutward>();
    }
    this.transferOutwards.add(transferOutwardsItem);
    return this;
  }

   /**
   * Used for search result and create only
   * @return transferOutwards
  **/
  @ApiModelProperty(value = "Used for search result and create only")

  @Valid

  public List<TransferOutward> getTransferOutwards() {
    return transferOutwards;
  }

  public void setTransferOutwards(List<TransferOutward> transferOutwards) {
    this.transferOutwards = transferOutwards;
  }

  public TransferOutwardResponse page(Page page) {
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
    TransferOutwardResponse transferOutwardResponse = (TransferOutwardResponse) o;
    return Objects.equals(this.responseInfo, transferOutwardResponse.responseInfo) &&
        Objects.equals(this.transferOutwards, transferOutwardResponse.transferOutwards) &&
        Objects.equals(this.page, transferOutwardResponse.page);
  }

  @Override
  public int hashCode() {
    return Objects.hash(responseInfo, transferOutwards, page);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TransferOutwardResponse {\n");
    
    sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
    sb.append("    transferOutwards: ").append(toIndentedString(transferOutwards)).append("\n");
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

