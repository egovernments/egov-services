package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.Page;
import io.swagger.model.ResponseInfo;
import io.swagger.model.TransferInward;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Contract class for web response. Array of TransferInward items  are used in case of search ,create or update request.
 */
@ApiModel(description = "Contract class for web response. Array of TransferInward items  are used in case of search ,create or update request.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-28T13:21:55.964+05:30")

public class TransferInwardResponse   {
  @JsonProperty("responseInfo")
  private ResponseInfo responseInfo = null;

  @JsonProperty("transferInwards")
  @Valid
  private List<TransferInward> transferInwards = null;

  @JsonProperty("page")
  private Page page = null;

  public TransferInwardResponse responseInfo(ResponseInfo responseInfo) {
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

  public TransferInwardResponse transferInwards(List<TransferInward> transferInwards) {
    this.transferInwards = transferInwards;
    return this;
  }

  public TransferInwardResponse addTransferInwardsItem(TransferInward transferInwardsItem) {
    if (this.transferInwards == null) {
      this.transferInwards = new ArrayList<TransferInward>();
    }
    this.transferInwards.add(transferInwardsItem);
    return this;
  }

   /**
   * Used for search result and create only
   * @return transferInwards
  **/
  @ApiModelProperty(value = "Used for search result and create only")

  @Valid

  public List<TransferInward> getTransferInwards() {
    return transferInwards;
  }

  public void setTransferInwards(List<TransferInward> transferInwards) {
    this.transferInwards = transferInwards;
  }

  public TransferInwardResponse page(Page page) {
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
    TransferInwardResponse transferInwardResponse = (TransferInwardResponse) o;
    return Objects.equals(this.responseInfo, transferInwardResponse.responseInfo) &&
        Objects.equals(this.transferInwards, transferInwardResponse.transferInwards) &&
        Objects.equals(this.page, transferInwardResponse.page);
  }

  @Override
  public int hashCode() {
    return Objects.hash(responseInfo, transferInwards, page);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TransferInwardResponse {\n");
    
    sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
    sb.append("    transferInwards: ").append(toIndentedString(transferInwards)).append("\n");
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

