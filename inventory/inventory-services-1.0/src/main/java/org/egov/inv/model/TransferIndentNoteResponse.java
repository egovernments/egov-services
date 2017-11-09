package org.egov.inv.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.egov.inv.model.Indent;
import org.egov.inv.model.Page;
import org.egov.inv.model.ResponseInfo;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Contract class for web response. Array of Indent items  are used in case of search ,create or update request.
 */
@ApiModel(description = "Contract class for web response. Array of Indent items  are used in case of search ,create or update request.")
@javax.annotation.Generated(value = "org.egov.inv.codegen.languages.SpringCodegen", date = "2017-11-08T13:51:07.770Z")

public class TransferIndentNoteResponse   {
  @JsonProperty("ResponseInfo")
  private ResponseInfo responseInfo = null;

  @JsonProperty("transferIndentNotes")
  private List<Indent> transferIndentNotes = null;

  @JsonProperty("page")
  private Page page = null;

  public TransferIndentNoteResponse responseInfo(ResponseInfo responseInfo) {
    this.responseInfo = responseInfo;
    return this;
  }

   /**
   * Get responseInfo
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

  public TransferIndentNoteResponse transferIndentNotes(List<Indent> transferIndentNotes) {
    this.transferIndentNotes = transferIndentNotes;
    return this;
  }

  public TransferIndentNoteResponse addTransferIndentNotesItem(Indent transferIndentNotesItem) {
    if (this.transferIndentNotes == null) {
      this.transferIndentNotes = new ArrayList<Indent>();
    }
    this.transferIndentNotes.add(transferIndentNotesItem);
    return this;
  }

   /**
   * Used for search result and create only
   * @return transferIndentNotes
  **/
  @ApiModelProperty(value = "Used for search result and create only")

  @Valid

  public List<Indent> getTransferIndentNotes() {
    return transferIndentNotes;
  }

  public void setTransferIndentNotes(List<Indent> transferIndentNotes) {
    this.transferIndentNotes = transferIndentNotes;
  }

  public TransferIndentNoteResponse page(Page page) {
    this.page = page;
    return this;
  }

   /**
   * Get page
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
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TransferIndentNoteResponse transferIndentNoteResponse = (TransferIndentNoteResponse) o;
    return Objects.equals(this.responseInfo, transferIndentNoteResponse.responseInfo) &&
        Objects.equals(this.transferIndentNotes, transferIndentNoteResponse.transferIndentNotes) &&
        Objects.equals(this.page, transferIndentNoteResponse.page);
  }

  @Override
  public int hashCode() {
    return Objects.hash(responseInfo, transferIndentNotes, page);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TransferIndentNoteResponse {\n");
    
    sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
    sb.append("    transferIndentNotes: ").append(toIndentedString(transferIndentNotes)).append("\n");
    sb.append("    page: ").append(toIndentedString(page)).append("\n");
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

