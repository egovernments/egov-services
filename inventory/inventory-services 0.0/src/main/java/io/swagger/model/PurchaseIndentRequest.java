package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.PurchaseIndent;
import io.swagger.model.RequestInfo;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Contract class for web request. Array of PurchaseIndent items  are used in case of create or update
 */
@ApiModel(description = "Contract class for web request. Array of PurchaseIndent items  are used in case of create or update")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-28T13:21:55.964+05:30")

public class PurchaseIndentRequest   {
  @JsonProperty("requestInfo")
  private RequestInfo requestInfo = null;

  @JsonProperty("purchaseIndents")
  @Valid
  private List<PurchaseIndent> purchaseIndents = null;

  public PurchaseIndentRequest requestInfo(RequestInfo requestInfo) {
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

  public PurchaseIndentRequest purchaseIndents(List<PurchaseIndent> purchaseIndents) {
    this.purchaseIndents = purchaseIndents;
    return this;
  }

  public PurchaseIndentRequest addPurchaseIndentsItem(PurchaseIndent purchaseIndentsItem) {
    if (this.purchaseIndents == null) {
      this.purchaseIndents = new ArrayList<PurchaseIndent>();
    }
    this.purchaseIndents.add(purchaseIndentsItem);
    return this;
  }

   /**
   * Used for search result and create only
   * @return purchaseIndents
  **/
  @ApiModelProperty(value = "Used for search result and create only")

  @Valid

  public List<PurchaseIndent> getPurchaseIndents() {
    return purchaseIndents;
  }

  public void setPurchaseIndents(List<PurchaseIndent> purchaseIndents) {
    this.purchaseIndents = purchaseIndents;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PurchaseIndentRequest purchaseIndentRequest = (PurchaseIndentRequest) o;
    return Objects.equals(this.requestInfo, purchaseIndentRequest.requestInfo) &&
        Objects.equals(this.purchaseIndents, purchaseIndentRequest.purchaseIndents);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestInfo, purchaseIndents);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PurchaseIndentRequest {\n");
    
    sb.append("    requestInfo: ").append(toIndentedString(requestInfo)).append("\n");
    sb.append("    purchaseIndents: ").append(toIndentedString(purchaseIndents)).append("\n");
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

