package org.egov.inv.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Contract class for web response. Array of PurchaseOrder items  are used in case of search ,create or update request.
 */
@ApiModel(description = "Contract class for web response. Array of PurchaseOrder items  are used in case of search ,create or update request.")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-27T06:37:03.617Z")

public class PurchaseOrderResponse {
    @JsonProperty("responseInfo")
    private ResponseInfo responseInfo = null;

    @JsonProperty("purchaseOrders")
    private List<PurchaseOrder> purchaseOrders = null;

    @JsonProperty("page")
    private Page page = null;

    public PurchaseOrderResponse responseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
        return this;
    }

    /**
     * Get responseInfo
     *
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

    public PurchaseOrderResponse purchaseOrders(List<PurchaseOrder> purchaseOrders) {
        this.purchaseOrders = purchaseOrders;
        return this;
    }

    public PurchaseOrderResponse addPurchaseOrdersItem(PurchaseOrder purchaseOrdersItem) {
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

    public PurchaseOrderResponse page(Page page) {
        this.page = page;
        return this;
    }

    /**
     * Get page
     *
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PurchaseOrderResponse purchaseOrderResponse = (PurchaseOrderResponse) o;
        return Objects.equals(this.responseInfo, purchaseOrderResponse.responseInfo) &&
                Objects.equals(this.purchaseOrders, purchaseOrderResponse.purchaseOrders) &&
                Objects.equals(this.page, purchaseOrderResponse.page);
    }

    @Override
    public int hashCode() {
        return Objects.hash(responseInfo, purchaseOrders, page);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class PurchaseOrderResponse {\n");

        sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
        sb.append("    purchaseOrders: ").append(toIndentedString(purchaseOrders)).append("\n");
        sb.append("    page: ").append(toIndentedString(page)).append("\n");
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

