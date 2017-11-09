package org.egov.inv.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.egov.inv.model.MaterialReceipt;
import org.egov.inv.model.Page;
import org.egov.inv.model.ResponseInfo;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Contract class for material receipt inward web response. Array of MaterialReceipt items  are used in case of search ,create or update request.
 */
@ApiModel(description = "Contract class for material receipt inward web response. Array of MaterialReceipt items  are used in case of search ,create or update request.")
@javax.annotation.Generated(value = "org.egov.inv.codegen.languages.SpringCodegen", date = "2017-11-08T13:51:07.770Z")

public class TransferInwardResponse   {
  @JsonProperty("ResponseInfo")
  private ResponseInfo responseInfo = null;

  @JsonProperty("transferInwards")
  private List<MaterialReceipt> transferInwards = null;

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

  public TransferInwardResponse transferInwards(List<MaterialReceipt> transferInwards) {
    this.transferInwards = transferInwards;
    return this;
  }

  public TransferInwardResponse addTransferInwardsItem(MaterialReceipt transferInwardsItem) {
    if (this.transferInwards == null) {
      this.transferInwards = new ArrayList<MaterialReceipt>();
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

  public List<MaterialReceipt> getTransferInwards() {
    return transferInwards;
  }

  public void setTransferInwards(List<MaterialReceipt> transferInwards) {
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

