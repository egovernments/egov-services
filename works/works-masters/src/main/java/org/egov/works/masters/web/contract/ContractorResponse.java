package org.egov.works.masters.web.contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Contract class to send response. Array of Contractor items are used in case of search results, also multiple Contractor item is used for create and update
 */
@ApiModel(description = "Contract class to send response. Array of Contractor items are used in case of search results, also multiple Contractor item is used for create and update")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-10T10:39:50.702Z")

public class ContractorResponse   {
  @JsonProperty("ResponseInfo")
  private ResponseInfo responseInfo = null;

  @JsonProperty("contractors")
  private List<Contractor> contractors = null;

  public ContractorResponse responseInfo(ResponseInfo responseInfo) {
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

  public ContractorResponse contractors(List<Contractor> contractors) {
    this.contractors = contractors;
    return this;
  }

  public ContractorResponse addContractorsItem(Contractor contractorsItem) {
    if (this.contractors == null) {
      this.contractors = new ArrayList<Contractor>();
    }
    this.contractors.add(contractorsItem);
    return this;
  }

   /**
   * Used for search result and create only
   * @return contractors
  **/
  @ApiModelProperty(value = "Used for search result and create only")

  @Valid

  public List<Contractor> getContractors() {
    return contractors;
  }

  public void setContractors(List<Contractor> contractors) {
    this.contractors = contractors;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ContractorResponse contractorResponse = (ContractorResponse) o;
    return Objects.equals(this.responseInfo, contractorResponse.responseInfo) &&
        Objects.equals(this.contractors, contractorResponse.contractors);
  }

  @Override
  public int hashCode() {
    return Objects.hash(responseInfo, contractors);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ContractorResponse {\n");
    
    sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
    sb.append("    contractors: ").append(toIndentedString(contractors)).append("\n");
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

