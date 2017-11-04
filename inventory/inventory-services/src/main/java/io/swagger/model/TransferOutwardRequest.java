package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.RequestInfo;
import io.swagger.model.TransferOutward;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Contract class for web request. Array of TransferOutward items  are used in case of create or update
 */
@ApiModel(description = "Contract class for web request. Array of TransferOutward items  are used in case of create or update")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-28T13:21:55.964+05:30")

public class TransferOutwardRequest   {
  @JsonProperty("requestInfo")
  private RequestInfo requestInfo = null;

  @JsonProperty("transferOutwards")
  @Valid
  private List<TransferOutward> transferOutwards = null;

  public TransferOutwardRequest requestInfo(RequestInfo requestInfo) {
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

  public TransferOutwardRequest transferOutwards(List<TransferOutward> transferOutwards) {
    this.transferOutwards = transferOutwards;
    return this;
  }

  public TransferOutwardRequest addTransferOutwardsItem(TransferOutward transferOutwardsItem) {
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TransferOutwardRequest transferOutwardRequest = (TransferOutwardRequest) o;
    return Objects.equals(this.requestInfo, transferOutwardRequest.requestInfo) &&
        Objects.equals(this.transferOutwards, transferOutwardRequest.transferOutwards);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestInfo, transferOutwards);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TransferOutwardRequest {\n");
    
    sb.append("    requestInfo: ").append(toIndentedString(requestInfo)).append("\n");
    sb.append("    transferOutwards: ").append(toIndentedString(transferOutwards)).append("\n");
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

