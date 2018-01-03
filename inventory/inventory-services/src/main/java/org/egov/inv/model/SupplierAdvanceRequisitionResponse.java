package org.egov.inv.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Contract class for web response. Array of SupplierAdvanceRequisition forms responses which are used in create, update, search.
 */
@ApiModel(description = "Contract class for web response. Array of SupplierAdvanceRequisition forms responses which are used in create, update, search.")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-01-03T10:57:16.070Z")

public class SupplierAdvanceRequisitionResponse   {
  @JsonProperty("ResponseInfo")
  private ResponseInfo responseInfo = null;

  @JsonProperty("supplierAdvanceRequisitions")
  private List<SupplierAdvanceRequisition> supplierAdvanceRequisitions = null;

  @JsonProperty("page")
  private Page page = null;

  public SupplierAdvanceRequisitionResponse responseInfo(ResponseInfo responseInfo) {
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

  public SupplierAdvanceRequisitionResponse supplierAdvanceRequisitions(List<SupplierAdvanceRequisition> supplierAdvanceRequisitions) {
    this.supplierAdvanceRequisitions = supplierAdvanceRequisitions;
    return this;
  }

  public SupplierAdvanceRequisitionResponse addSupplierAdvanceRequisitionsItem(SupplierAdvanceRequisition supplierAdvanceRequisitionsItem) {
    if (this.supplierAdvanceRequisitions == null) {
      this.supplierAdvanceRequisitions = new ArrayList<SupplierAdvanceRequisition>();
    }
    this.supplierAdvanceRequisitions.add(supplierAdvanceRequisitionsItem);
    return this;
  }

   /**
   * Used for search result and create only
   * @return supplierAdvanceRequisitions
  **/
  @ApiModelProperty(value = "Used for search result and create only")

  @Valid

  public List<SupplierAdvanceRequisition> getSupplierAdvanceRequisitions() {
    return supplierAdvanceRequisitions;
  }

  public void setSupplierAdvanceRequisitions(List<SupplierAdvanceRequisition> supplierAdvanceRequisitions) {
    this.supplierAdvanceRequisitions = supplierAdvanceRequisitions;
  }

  public SupplierAdvanceRequisitionResponse page(Page page) {
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
    SupplierAdvanceRequisitionResponse supplierAdvanceRequisitionResponse = (SupplierAdvanceRequisitionResponse) o;
    return Objects.equals(this.responseInfo, supplierAdvanceRequisitionResponse.responseInfo) &&
        Objects.equals(this.supplierAdvanceRequisitions, supplierAdvanceRequisitionResponse.supplierAdvanceRequisitions) &&
        Objects.equals(this.page, supplierAdvanceRequisitionResponse.page);
  }

  @Override
  public int hashCode() {
    return Objects.hash(responseInfo, supplierAdvanceRequisitions, page);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SupplierAdvanceRequisitionResponse {\n");
    
    sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
    sb.append("    supplierAdvanceRequisitions: ").append(toIndentedString(supplierAdvanceRequisitions)).append("\n");
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
