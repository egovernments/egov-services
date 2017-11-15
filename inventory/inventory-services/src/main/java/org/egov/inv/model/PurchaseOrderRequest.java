package org.egov.inv.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.egov.inv.model.PurchaseOrder;
import org.egov.inv.model.RequestInfo;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * PurchaseOrderRequest
 */
@javax.annotation.Generated(value = "org.egov.inv.codegen.languages.SpringCodegen", date = "2017-11-08T13:51:07.770Z")

public class PurchaseOrderRequest   {
  @JsonProperty("RequestInfo")
  private RequestInfo requestInfo = null;

  @JsonProperty("purchaseOrders")
  private List<PurchaseOrder> purchaseOrders = new ArrayList<PurchaseOrder>();

  public PurchaseOrderRequest requestInfo(RequestInfo requestInfo) {
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

  public PurchaseOrderRequest purchaseOrders(List<PurchaseOrder> purchaseOrders) {
    this.purchaseOrders = purchaseOrders;
    return this;
  }

  public PurchaseOrderRequest addPurchaseOrdersItem(PurchaseOrder purchaseOrdersItem) {
    this.purchaseOrders.add(purchaseOrdersItem);
    return this;
  }

   /**
   * Used for search result and create only
   * @return purchaseOrders
  **/
  @ApiModelProperty(required = true, value = "Used for search result and create only")
  @NotNull

  @Valid

  public List<PurchaseOrder> getPurchaseOrders() {
    return purchaseOrders;
  }

  public void setPurchaseOrders(List<PurchaseOrder> purchaseOrders) {
    this.purchaseOrders = purchaseOrders;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PurchaseOrderRequest purchaseOrderRequest = (PurchaseOrderRequest) o;
    return Objects.equals(this.requestInfo, purchaseOrderRequest.requestInfo) &&
        Objects.equals(this.purchaseOrders, purchaseOrderRequest.purchaseOrders);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestInfo, purchaseOrders);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PurchaseOrderRequest {\n");
    
    sb.append("    requestInfo: ").append(toIndentedString(requestInfo)).append("\n");
    sb.append("    purchaseOrders: ").append(toIndentedString(purchaseOrders)).append("\n");
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

