package org.egov.inv.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Contract class for web request. Array of PurchaseMaterial items  are used in case of create or update
 */
@ApiModel(description = "Contract class for web request. Array of PurchaseMaterial items  are used in case of create or update")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-27T06:37:03.617Z")

public class PurchaseMaterialRequest {
    @JsonProperty("requestInfo")
    private RequestInfo requestInfo = null;

    @JsonProperty("purchaseMaterials")
    private List<PurchaseMaterial> purchaseMaterials = null;

    public PurchaseMaterialRequest requestInfo(RequestInfo requestInfo) {
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

    public PurchaseMaterialRequest purchaseMaterials(List<PurchaseMaterial> purchaseMaterials) {
        this.purchaseMaterials = purchaseMaterials;
        return this;
    }

    public PurchaseMaterialRequest addPurchaseMaterialsItem(PurchaseMaterial purchaseMaterialsItem) {
        if (this.purchaseMaterials == null) {
            this.purchaseMaterials = new ArrayList<PurchaseMaterial>();
        }
        this.purchaseMaterials.add(purchaseMaterialsItem);
        return this;
    }

    /**
     * Used for search result and create only
     *
     * @return purchaseMaterials
     **/
    @ApiModelProperty(value = "Used for search result and create only")

    @Valid

    public List<PurchaseMaterial> getPurchaseMaterials() {
        return purchaseMaterials;
    }

    public void setPurchaseMaterials(List<PurchaseMaterial> purchaseMaterials) {
        this.purchaseMaterials = purchaseMaterials;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PurchaseMaterialRequest purchaseMaterialRequest = (PurchaseMaterialRequest) o;
        return Objects.equals(this.requestInfo, purchaseMaterialRequest.requestInfo) &&
                Objects.equals(this.purchaseMaterials, purchaseMaterialRequest.purchaseMaterials);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestInfo, purchaseMaterials);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class PurchaseMaterialRequest {\n");

        sb.append("    requestInfo: ").append(toIndentedString(requestInfo)).append("\n");
        sb.append("    purchaseMaterials: ").append(toIndentedString(purchaseMaterials)).append("\n");
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

