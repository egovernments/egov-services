package org.egov.works.measurementbook.web.contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Contract class to send response. Array of ContractorBill items are used in case of search results, whereas single ContractorBill item is used for create and update
 */
@ApiModel(description = "Contract class to send response. Array of ContractorBill items are used in case of search results, whereas single ContractorBill item is used for create and update")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-12-20T10:00:39.005Z")

public class ContractorBillRequest   {
  @JsonProperty("RequestInfo")
  private RequestInfo requestInfo = null;

  @JsonProperty("contractorBills")
  private List<ContractorBill> contractorBills = null;

  public ContractorBillRequest requestInfo(RequestInfo requestInfo) {
    this.requestInfo = requestInfo;
    return this;
  }

   /**
   * Get requestInfo
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

  public ContractorBillRequest contractorBills(List<ContractorBill> contractorBills) {
    this.contractorBills = contractorBills;
    return this;
  }

  public ContractorBillRequest addContractorBillsItem(ContractorBill contractorBillsItem) {
    if (this.contractorBills == null) {
      this.contractorBills = new ArrayList<ContractorBill>();
    }
    this.contractorBills.add(contractorBillsItem);
    return this;
  }

   /**
   * Used for create and update only
   * @return contractorBills
  **/
  @ApiModelProperty(value = "Used for create and update only")

  @Valid

  public List<ContractorBill> getContractorBills() {
    return contractorBills;
  }

  public void setContractorBills(List<ContractorBill> contractorBills) {
    this.contractorBills = contractorBills;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ContractorBillRequest contractorBillRequest = (ContractorBillRequest) o;
    return Objects.equals(this.requestInfo, contractorBillRequest.requestInfo) &&
        Objects.equals(this.contractorBills, contractorBillRequest.contractorBills);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestInfo, contractorBills);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ContractorBillRequest {\n");
    
    sb.append("    requestInfo: ").append(toIndentedString(requestInfo)).append("\n");
    sb.append("    contractorBills: ").append(toIndentedString(contractorBills)).append("\n");
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

