package org.egov.inv.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Contract class for web request. Array of TransferOutward items  are used in case of create or update
 */
@ApiModel(description = "Contract class for web request. Array of TransferOutward items  are used in case of create or update")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-27T06:37:03.617Z")

public class TransferOutwardRequest {
    @JsonProperty("requestInfo")
    private RequestInfo requestInfo = null;

    @JsonProperty("transferOutwards")
    private List<TransferOutward> transferOutwards = null;

    public TransferOutwardRequest requestInfo(RequestInfo requestInfo) {
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

    public TransferOutwardRequest transferOutwards(List<TransferOutward> transferOutwards) {
        this.transferOutwards = transferOutwards;
        return this;
    }

    public TransferOutwardRequest addTransferOutwardsItem(TransferOutward transferOutwardsItem) {
        if (this.transferOutwards == null) {
            this.transferOutwards = new ArrayList<TransferOutward>();
        }
        this.transferOutwards.add(transferOutwardsItem);
        return this;
    }

    /**
     * Used for search result and create only
     *
     * @return transferOutwards
     **/
    @ApiModelProperty(value = "Used for search result and create only")

    @Valid

    public List<TransferOutward> getTransferOutwards() {
        return transferOutwards;
    }

    public void setTransferOutwards(List<TransferOutward> transferOutwards) {
        this.transferOutwards = transferOutwards;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TransferOutwardRequest transferOutwardRequest = (TransferOutwardRequest) o;
        return Objects.equals(this.requestInfo, transferOutwardRequest.requestInfo) &&
                Objects.equals(this.transferOutwards, transferOutwardRequest.transferOutwards);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestInfo, transferOutwards);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class TransferOutwardRequest {\n");

        sb.append("    requestInfo: ").append(toIndentedString(requestInfo)).append("\n");
        sb.append("    transferOutwards: ").append(toIndentedString(transferOutwards)).append("\n");
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

