package org.egov.inv.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Contract class for web request. Array of SupplierAdvanceRequisition form requests for create, update.
 */
@ApiModel(description = "Contract class for web request. Array of SupplierAdvanceRequisition form requests for create, update.")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-01-03T10:57:16.070Z")

public class SupplierAdvanceRequisitionRequest   {
  @JsonProperty("RequestInfo")
  private RequestInfo requestInfo = null;

  @JsonProperty("supplierAdvanceRequisitions")
  private List<SupplierAdvanceRequisition> supplierAdvanceRequisitions = new ArrayList<SupplierAdvanceRequisition>();

  public SupplierAdvanceRequisitionRequest requestInfo(RequestInfo requestInfo) {
    this.requestInfo = requestInfo;
    return this;
  }

   /**
   * Get requestInfo
   * @return requestInfo
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull


  public RequestInfo getRequestInfo() {
    return requestInfo;
  }

  public void setRequestInfo(RequestInfo requestInfo) {
    this.requestInfo = requestInfo;
  }

  public SupplierAdvanceRequisitionRequest supplierAdvanceRequisitions(List<SupplierAdvanceRequisition> supplierAdvanceRequisitions) {
    this.supplierAdvanceRequisitions = supplierAdvanceRequisitions;
    return this;
  }

  public SupplierAdvanceRequisitionRequest addSupplierAdvanceRequisitionsItem(SupplierAdvanceRequisition supplierAdvanceRequisitionsItem) {
    this.supplierAdvanceRequisitions.add(supplierAdvanceRequisitionsItem);
    return this;
  }

   /**
   * Used for search result and create only
   * @return supplierAdvanceRequisitions
  **/
  @ApiModelProperty(required = true, value = "Used for search result and create only")
  @NotNull

  public List<SupplierAdvanceRequisition> getSupplierAdvanceRequisitions() {
    return supplierAdvanceRequisitions;
  }

  public void setSupplierAdvanceRequisitions(List<SupplierAdvanceRequisition> supplierAdvanceRequisitions) {
    this.supplierAdvanceRequisitions = supplierAdvanceRequisitions;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SupplierAdvanceRequisitionRequest supplierAdvanceRequisitionRequest = (SupplierAdvanceRequisitionRequest) o;
    return Objects.equals(this.requestInfo, supplierAdvanceRequisitionRequest.requestInfo) &&
        Objects.equals(this.supplierAdvanceRequisitions, supplierAdvanceRequisitionRequest.supplierAdvanceRequisitions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestInfo, supplierAdvanceRequisitions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SupplierAdvanceRequisitionRequest {\n");
    
    sb.append("    requestInfo: ").append(toIndentedString(requestInfo)).append("\n");
    sb.append("    supplierAdvanceRequisitions: ").append(toIndentedString(supplierAdvanceRequisitions)).append("\n");
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
