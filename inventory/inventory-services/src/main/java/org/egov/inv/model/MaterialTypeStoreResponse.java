package org.egov.inv.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Contract class for web response. Array of Material Type Store Mapping are used in case of search ,create or update request.
 */
@ApiModel(description = "Contract class for web response. Array of Material Type Store Mapping are used in case of search ,create or update request.")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-12-14T09:31:14.044Z")

public class MaterialTypeStoreResponse {
    @JsonProperty("ResponseInfo")
    private ResponseInfo responseInfo = null;

    @JsonProperty("materialTypeStores")
    private List<MaterialTypeStoreMapping> materialTypeStores = null;

    @JsonProperty("page")
    private Page page = null;

    public MaterialTypeStoreResponse responseInfo(ResponseInfo responseInfo) {
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

    public MaterialTypeStoreResponse materialTypeStores(List<MaterialTypeStoreMapping> materialTypeStores) {
        this.materialTypeStores = materialTypeStores;
        return this;
    }

    public MaterialTypeStoreResponse addMaterialTypeStoresItem(MaterialTypeStoreMapping materialTypeStoresItem) {
        if (this.materialTypeStores == null) {
            this.materialTypeStores = new ArrayList<MaterialTypeStoreMapping>();
        }
        this.materialTypeStores.add(materialTypeStoresItem);
        return this;
    }

    /**
     * Used for search result and create only
     *
     * @return materialTypeStores
     **/
    @ApiModelProperty(value = "Used for search result and create only")

    @Valid

    public List<MaterialTypeStoreMapping> getMaterialTypeStores() {
        return materialTypeStores;
    }

    public void setMaterialTypeStores(List<MaterialTypeStoreMapping> materialTypeStores) {
        this.materialTypeStores = materialTypeStores;
    }

    public MaterialTypeStoreResponse page(Page page) {
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
        MaterialTypeStoreResponse materialTypeStoreResponse = (MaterialTypeStoreResponse) o;
        return Objects.equals(this.responseInfo, materialTypeStoreResponse.responseInfo) &&
                Objects.equals(this.materialTypeStores, materialTypeStoreResponse.materialTypeStores) &&
                Objects.equals(this.page, materialTypeStoreResponse.page);
    }

    @Override
    public int hashCode() {
        return Objects.hash(responseInfo, materialTypeStores, page);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class MaterialTypeStoreResponse {\n");

        sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
        sb.append("    materialTypeStores: ").append(toIndentedString(materialTypeStores)).append("\n");
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

