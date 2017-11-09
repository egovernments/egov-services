package org.egov.inv.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.egov.inv.model.Disposal;
import org.egov.inv.model.Page;
import org.egov.inv.model.ResponseInfo;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Contract class for web response. Array of Disposal items  are used in case of search ,create or update request.
 */
@ApiModel(description = "Contract class for web response. Array of Disposal items  are used in case of search ,create or update request.")
@javax.annotation.Generated(value = "org.egov.inv.codegen.languages.SpringCodegen", date = "2017-11-08T13:51:07.770Z")

public class DisposalResponse   {
  @JsonProperty("ResponseInfo")
  private ResponseInfo responseInfo = null;

  @JsonProperty("disposals")
  private List<Disposal> disposals = null;

  @JsonProperty("page")
  private Page page = null;

  public DisposalResponse responseInfo(ResponseInfo responseInfo) {
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

  public DisposalResponse disposals(List<Disposal> disposals) {
    this.disposals = disposals;
    return this;
  }

  public DisposalResponse addDisposalsItem(Disposal disposalsItem) {
    if (this.disposals == null) {
      this.disposals = new ArrayList<Disposal>();
    }
    this.disposals.add(disposalsItem);
    return this;
  }

   /**
   * Used for search result and create only
   * @return disposals
  **/
  @ApiModelProperty(value = "Used for search result and create only")

  @Valid

  public List<Disposal> getDisposals() {
    return disposals;
  }

  public void setDisposals(List<Disposal> disposals) {
    this.disposals = disposals;
  }

  public DisposalResponse page(Page page) {
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
    DisposalResponse disposalResponse = (DisposalResponse) o;
    return Objects.equals(this.responseInfo, disposalResponse.responseInfo) &&
        Objects.equals(this.disposals, disposalResponse.disposals) &&
        Objects.equals(this.page, disposalResponse.page);
  }

  @Override
  public int hashCode() {
    return Objects.hash(responseInfo, disposals, page);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DisposalResponse {\n");
    
    sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
    sb.append("    disposals: ").append(toIndentedString(disposals)).append("\n");
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

