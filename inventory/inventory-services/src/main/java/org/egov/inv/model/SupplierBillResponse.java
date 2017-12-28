package org.egov.inv.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Hold the supplier bill response information.
 */
@ApiModel(description = "Hold the supplier bill response information.")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-12-28T09:20:06.607Z")

public class SupplierBillResponse {
    @JsonProperty("ResponseInfo")
    private ResponseInfo responseInfo = null;

    @JsonProperty("supplierBills")
    private List<SupplierBill> supplierBills = null;

    public SupplierBillResponse responseInfo(ResponseInfo responseInfo) {
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

    public SupplierBillResponse supplierBills(List<SupplierBill> supplierBills) {
        this.supplierBills = supplierBills;
        return this;
    }

    public SupplierBillResponse addSupplierBillsItem(SupplierBill supplierBillsItem) {
        if (this.supplierBills == null) {
            this.supplierBills = new ArrayList<SupplierBill>();
        }
        this.supplierBills.add(supplierBillsItem);
        return this;
    }

    /**
     * Get supplierBills
     *
     * @return supplierBills
     **/
    @ApiModelProperty(value = "")

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
        SupplierBillResponse supplierBillResponse = (SupplierBillResponse) o;
        return Objects.equals(this.responseInfo, supplierBillResponse.responseInfo) &&
                Objects.equals(this.supplierBills, supplierBillResponse.supplierBills);
    }

    @Override
    public int hashCode() {
        return Objects.hash(responseInfo, supplierBills);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class SupplierBillResponse {\n");

        sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
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

