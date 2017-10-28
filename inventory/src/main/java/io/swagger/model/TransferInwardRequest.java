package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.RequestInfo;
import io.swagger.model.TransferInward;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Contract class for web request. Array of TransferInward items  are used in case of create or update
 */
@ApiModel(description = "Contract class for web request. Array of TransferInward items  are used in case of create or update")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-28T13:21:55.964+05:30")

public class TransferInwardRequest   {
  @JsonProperty("requestInfo")
  private RequestInfo requestInfo = null;

  @JsonProperty("transferInwards")
  @Valid
  private List<TransferInward> transferInwards = null;

  public TransferInwardRequest requestInfo(RequestInfo requestInfo) {
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

  public TransferInwardRequest transferInwards(List<TransferInward> transferInwards) {
    this.transferInwards = transferInwards;
    return this;
  }

  public TransferInwardRequest addTransferInwardsItem(TransferInward transferInwardsItem) {
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

