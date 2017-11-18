package org.egov.inv.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Hold the Material Receipt Note Response information
 */
@ApiModel(description = "Hold the Material Receipt Note Response information")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-18T03:45:26.890Z")

public class MaterialReceiptResponse {
    @JsonProperty("ResponseInfo")
    private ResponseInfo responseInfo = null;

    @JsonProperty("MaterialReceipt")
    private List<MaterialReceipt> materialReceipt = null;

    public MaterialReceiptResponse responseInfo(ResponseInfo responseInfo) {
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

    public MaterialReceiptResponse materialReceipt(List<MaterialReceipt> materialReceipt) {
        this.materialReceipt = materialReceipt;
        return this;
    }

    public MaterialReceiptResponse addMaterialReceiptItem(MaterialReceipt materialReceiptItem) {
        if (this.materialReceipt == null) {
            this.materialReceipt = new ArrayList<MaterialReceipt>();
        }
        this.materialReceipt.add(materialReceiptItem);
        return this;
    }

    /**
     * Get materialReceipt
     *
     * @return materialReceipt
     **/
    @ApiModelProperty(value = "")

    @Valid

    public List<MaterialReceipt> getMaterialReceipt() {
        return materialReceipt;
    }

    public void setMaterialReceipt(List<MaterialReceipt> materialReceipt) {
        this.materialReceipt = materialReceipt;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MaterialReceiptResponse materialReceiptResponse = (MaterialReceiptResponse) o;
        return Objects.equals(this.responseInfo, materialReceiptResponse.responseInfo) &&
                Objects.equals(this.materialReceipt, materialReceiptResponse.materialReceipt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(responseInfo, materialReceipt);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class MaterialReceiptResponse {\n");

        sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
        sb.append("    materialReceipt: ").append(toIndentedString(materialReceipt)).append("\n");
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
