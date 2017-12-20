package org.egov.inv.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import java.util.Objects;

/**
 * Contract class to check whether object used in transaction .
 */
@ApiModel(description = "Contract class to check whether object used in transaction .")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-12-20T12:02:45.458Z")

public class TransactionUsedResponse {
    @JsonProperty("ResponseInfo")
    private ResponseInfo responseInfo = null;

    @JsonProperty("transactionUsed")
    private Boolean transactionUsed = null;

    public TransactionUsedResponse responseInfo(ResponseInfo responseInfo) {
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

    public TransactionUsedResponse transactionUsed(Boolean transactionUsed) {
        this.transactionUsed = transactionUsed;
        return this;
    }

    /**
     * Used to check whether object used in transaction .
     *
     * @return transactionUsed
     **/
    @ApiModelProperty(value = "Used to check whether object used in transaction .")


    public Boolean getTransactionUsed() {
        return transactionUsed;
    }

    public void setTransactionUsed(Boolean transactionUsed) {
        this.transactionUsed = transactionUsed;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TransactionUsedResponse transactionUsedResponse = (TransactionUsedResponse) o;
        return Objects.equals(this.responseInfo, transactionUsedResponse.responseInfo) &&
                Objects.equals(this.transactionUsed, transactionUsedResponse.transactionUsed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(responseInfo, transactionUsed);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class TransactionUsedResponse {\n");

        sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
        sb.append("    transactionUsed: ").append(toIndentedString(transactionUsed)).append("\n");
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

