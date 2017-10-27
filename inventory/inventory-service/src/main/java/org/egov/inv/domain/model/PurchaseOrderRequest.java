package org.egov.inv.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * PurchaseOrderRequest
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-27T06:37:03.617Z")

public class PurchaseOrderRequest {
    @JsonProperty("requestInfo")
    private RequestInfo requestInfo = null;

    @JsonProperty("purchaseOrders")
    private List<PurchaseOrder> purchaseOrders = null;

    public PurchaseOrderRequest requestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    /**
     * Get requestInfo
     *
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

    public PurchaseOrderRequest purchaseOrders(List<PurchaseOrder> purchaseOrders) {
        this.purchaseOrders = purchaseOrders;
        return this;
    }

    public PurchaseOrderRequest addPurchaseOrdersItem(PurchaseOrder purchaseOrdersItem) {
        if (this.purchaseOrders == null) {
            this.purchaseOrders = new ArrayList<PurchaseOrder>();
        }
        this.purchaseOrders.add(purchaseOrdersItem);
        return this;
    }

    /**
     * Used for search result and create only
     *
     * @return purchaseOrders
     **/
    @ApiModelProperty(value = "Used for search result and create only")

    @Valid

    public List<PurchaseOrder> getPurchaseOrders() {
        return purchaseOrders;
    }

    public void setPurchaseOrders(List<PurchaseOrder> purchaseOrders) {
        this.purchaseOrders = purchaseOrders;
    }


    @Override
    public boolean equals(Object o) {
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
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}

