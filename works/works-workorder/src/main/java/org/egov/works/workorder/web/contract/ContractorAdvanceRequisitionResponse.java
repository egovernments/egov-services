package org.egov.works.workorder.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Contract class to send response. Array of ContractorAdvanceRequisition items are used in case of search results, whereas single ContractorAdvanceRequisition item is used for create and update
 */
@ApiModel(description = "Contract class to send response. Array of ContractorAdvanceRequisition items are used in case of search results, whereas single ContractorAdvanceRequisition item is used for create and update")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-30T11:45:24.744Z")

public class ContractorAdvanceRequisitionResponse {
    @JsonProperty("ResponseInfo")
    private ResponseInfo responseInfo = null;

    @JsonProperty("contractorAdvanceRequisitions")
    private List<ContractorAdvanceRequisition> contractorAdvanceRequisitions = null;

    public ContractorAdvanceRequisitionResponse responseInfo(ResponseInfo responseInfo) {
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

    public ContractorAdvanceRequisitionResponse contractorAdvanceRequisitions(List<ContractorAdvanceRequisition> contractorAdvanceRequisitions) {
        this.contractorAdvanceRequisitions = contractorAdvanceRequisitions;
        return this;
    }

    public ContractorAdvanceRequisitionResponse addContractorAdvanceRequisitionsItem(ContractorAdvanceRequisition contractorAdvanceRequisitionsItem) {
        if (this.contractorAdvanceRequisitions == null) {
            this.contractorAdvanceRequisitions = new ArrayList<ContractorAdvanceRequisition>();
        }
        this.contractorAdvanceRequisitions.add(contractorAdvanceRequisitionsItem);
        return this;
    }

    /**
     * Used for search result and create only
     *
     * @return contractorAdvanceRequisitions
     **/
    @ApiModelProperty(value = "Used for search result and create only")

    @Valid

    public List<ContractorAdvanceRequisition> getContractorAdvanceRequisitions() {
        return contractorAdvanceRequisitions;
    }

    public void setContractorAdvanceRequisitions(List<ContractorAdvanceRequisition> contractorAdvanceRequisitions) {
        this.contractorAdvanceRequisitions = contractorAdvanceRequisitions;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ContractorAdvanceRequisitionResponse contractorAdvanceRequisitionResponse = (ContractorAdvanceRequisitionResponse) o;
        return Objects.equals(this.responseInfo, contractorAdvanceRequisitionResponse.responseInfo) &&
                Objects.equals(this.contractorAdvanceRequisitions, contractorAdvanceRequisitionResponse.contractorAdvanceRequisitions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(responseInfo, contractorAdvanceRequisitions);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ContractorAdvanceRequisitionResponse {\n");

        sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
        sb.append("    contractorAdvanceRequisitions: ").append(toIndentedString(contractorAdvanceRequisitions)).append("\n");
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

