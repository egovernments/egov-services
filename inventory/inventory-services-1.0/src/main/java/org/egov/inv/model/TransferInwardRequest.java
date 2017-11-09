package org.egov.inv.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.egov.inv.model.MaterialReceipt;
import org.egov.inv.model.RequestInfo;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Contract class for material receipt inward request. Array of MaterialReceipt items  are used in case of create or update
 */
@ApiModel(description = "Contract class for material receipt inward request. Array of MaterialReceipt items  are used in case of create or update")
@javax.annotation.Generated(value = "org.egov.inv.codegen.languages.SpringCodegen", date = "2017-11-08T13:51:07.770Z")

public class TransferInwardRequest   {
  @JsonProperty("RequestInfo")
  private RequestInfo requestInfo = null;

  @JsonProperty("transferInwards")
  private List<MaterialReceipt> transferInwards = new ArrayList<MaterialReceipt>();

  public TransferInwardRequest requestInfo(RequestInfo requestInfo) {
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

  public TransferInwardRequest transferInwards(List<MaterialReceipt> transferInwards) {
    this.transferInwards = transferInwards;
    return this;
  }

  public TransferInwardRequest addTransferInwardsItem(MaterialReceipt transferInwardsItem) {
    this.transferInwards.add(transferInwardsItem);
    return this;
  }

   /**
   * Used for search result and create only
   * @return transferInwards
  **/
  @ApiModelProperty(required = true, value = "Used for search result and create only")
  @NotNull

  @Valid

  public List<MaterialReceipt> getTransferInwards() {
    return transferInwards;
  }

  public void setTransferInwards(List<MaterialReceipt> transferInwards) {
    this.transferInwards = transferInwards;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TransferInwardRequest transferInwardRequest = (TransferInwardRequest) o;
    return Objects.equals(this.requestInfo, transferInwardRequest.requestInfo) &&
        Objects.equals(this.transferInwards, transferInwardRequest.transferInwards);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestInfo, transferInwards);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TransferInwardRequest {\n");
    
    sb.append("    requestInfo: ").append(toIndentedString(requestInfo)).append("\n");
    sb.append("    transferInwards: ").append(toIndentedString(transferInwards)).append("\n");
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

