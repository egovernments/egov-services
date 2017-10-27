package org.egov.inv.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Contract class for web request. Array of TransferIndentNote items  are used in case of create or update
 */
@ApiModel(description = "Contract class for web request. Array of TransferIndentNote items  are used in case of create or update")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-27T06:37:03.617Z")

public class TransferIndentNoteRequest {
    @JsonProperty("requestInfo")
    private RequestInfo requestInfo = null;

    @JsonProperty("transferIndentNotes")
    private List<TransferIndentNote> transferIndentNotes = null;

    public TransferIndentNoteRequest requestInfo(RequestInfo requestInfo) {
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

    public TransferIndentNoteRequest transferIndentNotes(List<TransferIndentNote> transferIndentNotes) {
        this.transferIndentNotes = transferIndentNotes;
        return this;
    }

    public TransferIndentNoteRequest addTransferIndentNotesItem(TransferIndentNote transferIndentNotesItem) {
        if (this.transferIndentNotes == null) {
            this.transferIndentNotes = new ArrayList<TransferIndentNote>();
        }
        this.transferIndentNotes.add(transferIndentNotesItem);
        return this;
    }

    /**
     * Used for search result and create only
     *
     * @return transferIndentNotes
     **/
    @ApiModelProperty(value = "Used for search result and create only")

    @Valid

    public List<TransferIndentNote> getTransferIndentNotes() {
        return transferIndentNotes;
    }

    public void setTransferIndentNotes(List<TransferIndentNote> transferIndentNotes) {
        this.transferIndentNotes = transferIndentNotes;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TransferIndentNoteRequest transferIndentNoteRequest = (TransferIndentNoteRequest) o;
        return Objects.equals(this.requestInfo, transferIndentNoteRequest.requestInfo) &&
                Objects.equals(this.transferIndentNotes, transferIndentNoteRequest.transferIndentNotes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestInfo, transferIndentNotes);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class TransferIndentNoteRequest {\n");

        sb.append("    requestInfo: ").append(toIndentedString(requestInfo)).append("\n");
        sb.append("    transferIndentNotes: ").append(toIndentedString(transferIndentNotes)).append("\n");
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

