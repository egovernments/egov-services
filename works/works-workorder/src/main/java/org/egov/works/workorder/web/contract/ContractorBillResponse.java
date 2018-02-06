package org.egov.works.workorder.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Contract class to send response. Array of ContractorBill items are used in case of search results, whereas single ContractorBill item is used for create and update
 */
@ApiModel(description = "Contract class to send response. Array of ContractorBill items are used in case of search results, whereas single ContractorBill item is used for create and update")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-12-20T10:00:39.005Z")

public class ContractorBillResponse   {
  @JsonProperty("ResponseInfo")
  private ResponseInfo responseInfo = null;

  @JsonProperty("contractorBills")
  private List<ContractorBill> contractorBills = null;

  public ContractorBillResponse responseInfo(ResponseInfo responseInfo) {
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

  public ContractorBillResponse contractorBills(List<ContractorBill> contractorBills) {
    this.contractorBills = contractorBills;
    return this;
  }

  public ContractorBillResponse addContractorBillsItem(ContractorBill contractorBillsItem) {
    if (this.contractorBills == null) {
      this.contractorBills = new ArrayList<ContractorBill>();
    }
    this.contractorBills.add(contractorBillsItem);
    return this;
  }

   /**
   * Used for search result and create only
   * @return contractorBills
  **/
  @ApiModelProperty(value = "Used for search result and create only")

  @Valid

  public List<ContractorBill> getContractorBills() {
    return contractorBills;
  }

  public void setContractorBills(List<ContractorBill> contractorBills) {
    this.contractorBills = contractorBills;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ContractorBillResponse contractorBillResponse = (ContractorBillResponse) o;
    return Objects.equals(this.responseInfo, contractorBillResponse.responseInfo) &&
        Objects.equals(this.contractorBills, contractorBillResponse.contractorBills);
  }

  @Override
  public int hashCode() {
    return Objects.hash(responseInfo, contractorBills);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ContractorBillResponse {\n");

    sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
    sb.append("    contractorBills: ").append(toIndentedString(contractorBills)).append("\n");
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

