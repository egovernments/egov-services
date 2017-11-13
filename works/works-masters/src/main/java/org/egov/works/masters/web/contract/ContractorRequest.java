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

public class ContractorRequest   {
  @JsonProperty("RequestInfo")
  private RequestInfo requestInfo = null;

  @JsonProperty("contractors")
  private List<Contractor> contractors = null;

  public ContractorRequest requestInfo(RequestInfo requestInfo) {
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

  public ContractorRequest contractors(List<Contractor> contractors) {
    this.contractors = contractors;
    return this;
  }

  public ContractorRequest addContractorsItem(Contractor contractorsItem) {
    if (this.contractors == null) {
      this.contractors = new ArrayList<Contractor>();
    }
    this.contractors.add(contractorsItem);
    return this;
  }

   /**
   * Used for create and update only
   * @return contractors
  **/
  @ApiModelProperty(value = "Used for create and update only")

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
    ContractorRequest contractorRequest = (ContractorRequest) o;
    return Objects.equals(this.requestInfo, contractorRequest.requestInfo) &&
        Objects.equals(this.contractors, contractorRequest.contractors);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestInfo, contractors);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ContractorRequest {\n");
    
    sb.append("    requestInfo: ").append(toIndentedString(requestInfo)).append("\n");
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

