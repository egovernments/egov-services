package org.egov.inv.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Hold the supplier bill request information.
 */
@ApiModel(description = "Hold the supplier bill request information.")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-12-28T09:20:06.607Z")

public class SupplierBillRequest {
    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo = null;

    @JsonProperty("supplierBills")
    private List<SupplierBill> supplierBills = new ArrayList<SupplierBill>();

    public SupplierBillRequest requestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    /**
     * Get requestInfo
     *
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

    public SupplierBillRequest supplierBills(List<SupplierBill> supplierBills) {
        this.supplierBills = supplierBills;
        return this;
    }

    public SupplierBillRequest addSupplierBillsItem(SupplierBill supplierBillsItem) {
        this.supplierBills.add(supplierBillsItem);
        return this;
    }

    /**
     * Get supplierBills
     *
     * @return supplierBills
     **/
    @ApiModelProperty(required = true, value = "")
    @NotNull

    @Valid

    public List<SupplierBill> getSupplierBills() {
        return supplierBills;
    }

    public void setSupplierBills(List<SupplierBill> supplierBills) {
        this.supplierBills = supplierBills;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SupplierBillRequest supplierBillRequest = (SupplierBillRequest) o;
        return Objects.equals(this.requestInfo, supplierBillRequest.requestInfo) &&
                Objects.equals(this.supplierBills, supplierBillRequest.supplierBills);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestInfo, supplierBills);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class SupplierBillRequest {\n");

        sb.append("    requestInfo: ").append(toIndentedString(requestInfo)).append("\n");
        sb.append("    supplierBills: ").append(toIndentedString(supplierBills)).append("\n");
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

