package io.swagger.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Contract class for web response. Array of Material Store Mapping items  are used in case of search ,create or update request.
 */
@ApiModel(description = "Contract class for web response. Array of Material Store Mapping items  are used in case of search ,create or update request.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-02T16:27:56.269+05:30")
@Builder
public class MaterialStoreMappingResponse {
    @JsonProperty("responseInfo")
    private org.egov.common.contract.response.ResponseInfo responseInfo = null;

    @JsonProperty("materialStoreMappings")
    @Valid
    private List<MaterialStoreMapping> materialStoreMappings = null;

    @JsonProperty("page")
    private Page page = null;

    public MaterialStoreMappingResponse responseInfo(org.egov.common.contract.response.ResponseInfo responseInfo) {
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

    public org.egov.common.contract.response.ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(org.egov.common.contract.response.ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public MaterialStoreMappingResponse materialStoreMappings(List<MaterialStoreMapping> materialStoreMappings) {
        this.materialStoreMappings = materialStoreMappings;
        return this;
    }

    public MaterialStoreMappingResponse addMaterialStoreMappingsItem(MaterialStoreMapping materialStoreMappingsItem) {
        if (this.materialStoreMappings == null) {
            this.materialStoreMappings = new ArrayList<MaterialStoreMapping>();
        }
        this.materialStoreMappings.add(materialStoreMappingsItem);
        return this;
    }

    /**
     * Used for search result and create only
     *
     * @return materialStoreMappings
     **/
    @ApiModelProperty(value = "Used for search result and create only")

    @Valid

    public List<MaterialStoreMapping> getMaterialStoreMappings() {
        return materialStoreMappings;
    }

    public void setMaterialStoreMappings(List<MaterialStoreMapping> materialStoreMappings) {
        this.materialStoreMappings = materialStoreMappings;
    }

    public MaterialStoreMappingResponse page(Page page) {
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
        MaterialStoreMappingResponse materialStoreMappingResponse = (MaterialStoreMappingResponse) o;
        return Objects.equals(this.responseInfo, materialStoreMappingResponse.responseInfo) &&
                Objects.equals(this.materialStoreMappings, materialStoreMappingResponse.materialStoreMappings) &&
                Objects.equals(this.page, materialStoreMappingResponse.page);
    }

    @Override
    public int hashCode() {
        return Objects.hash(responseInfo, materialStoreMappings, page);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class MaterialStoreMappingResponse {\n");

        sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
        sb.append("    materialStoreMappings: ").append(toIndentedString(materialStoreMappings)).append("\n");
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

