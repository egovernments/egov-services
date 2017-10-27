package org.egov.inv.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Contract class for web response. Array of Supplier items  are used in case of search ,create or update request.
 */
@ApiModel(description = "Contract class for web response. Array of Supplier items  are used in case of search ,create or update request.")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-27T06:37:03.617Z")

public class SupplierResponse {
    @JsonProperty("responseInfo")
    private ResponseInfo responseInfo = null;

    @JsonProperty("suppliers")
    private List<Supplier> suppliers = null;

    @JsonProperty("page")
    private Page page = null;

    public SupplierResponse responseInfo(ResponseInfo responseInfo) {
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

    public SupplierResponse suppliers(List<Supplier> suppliers) {
        this.suppliers = suppliers;
        return this;
    }

    public SupplierResponse addSuppliersItem(Supplier suppliersItem) {
        if (this.suppliers == null) {
            this.suppliers = new ArrayList<Supplier>();
        }
        this.suppliers.add(suppliersItem);
        return this;
    }

    /**
     * Used for search result and create only
     *
     * @return suppliers
     **/
    @ApiModelProperty(value = "Used for search result and create only")

    @Valid

    public List<Supplier> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(List<Supplier> suppliers) {
        this.suppliers = suppliers;
    }

    public SupplierResponse page(Page page) {
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
        SupplierResponse supplierResponse = (SupplierResponse) o;
        return Objects.equals(this.responseInfo, supplierResponse.responseInfo) &&
                Objects.equals(this.suppliers, supplierResponse.suppliers) &&
                Objects.equals(this.page, supplierResponse.page);
    }

    @Override
    public int hashCode() {
        return Objects.hash(responseInfo, suppliers, page);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class SupplierResponse {\n");

        sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
        sb.append("    suppliers: ").append(toIndentedString(suppliers)).append("\n");
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

