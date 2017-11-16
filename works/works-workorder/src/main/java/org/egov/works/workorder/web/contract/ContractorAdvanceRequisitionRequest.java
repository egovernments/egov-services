package org.egov.works.workorder.web.contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Contract class to send response. Array of ContractorAdvanceRequisition items
 * are used in case of search results, whereas single
 * ContractorAdvanceRequisition item is used for create and update
 */
@ApiModel(description = "Contract class to send response. Array of ContractorAdvanceRequisition items are used in case of search results, whereas single ContractorAdvanceRequisition item is used for create and update")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-15T10:30:35.628Z")

public class ContractorAdvanceRequisitionRequest {
	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo = null;

	@JsonProperty("contractorAdvanceRequisitions")
	private List<ContractorAdvanceRequisition> contractorAdvanceRequisitions = null;

	public ContractorAdvanceRequisitionRequest requestInfo(RequestInfo requestInfo) {
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

	public ContractorAdvanceRequisitionRequest contractorAdvanceRequisitions(
			List<ContractorAdvanceRequisition> contractorAdvanceRequisitions) {
		this.contractorAdvanceRequisitions = contractorAdvanceRequisitions;
		return this;
	}

	public ContractorAdvanceRequisitionRequest addContractorAdvanceRequisitionsItem(
			ContractorAdvanceRequisition contractorAdvanceRequisitionsItem) {
		if (this.contractorAdvanceRequisitions == null) {
			this.contractorAdvanceRequisitions = new ArrayList<ContractorAdvanceRequisition>();
		}
		this.contractorAdvanceRequisitions.add(contractorAdvanceRequisitionsItem);
		return this;
	}

	/**
	 * Used for create and update only
	 * 
	 * @return contractorAdvanceRequisitions
	 **/
	@ApiModelProperty(value = "Used for create and update only")

	@Valid

	public List<ContractorAdvanceRequisition> getContractorAdvanceRequisitions() {
		return contractorAdvanceRequisitions;
	}

	public void setContractorAdvanceRequisitions(List<ContractorAdvanceRequisition> contractorAdvanceRequisitions) {
		this.contractorAdvanceRequisitions = contractorAdvanceRequisitions;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ContractorAdvanceRequisitionRequest contractorAdvanceRequisitionRequest = (ContractorAdvanceRequisitionRequest) o;
		return Objects.equals(this.requestInfo, contractorAdvanceRequisitionRequest.requestInfo) && Objects.equals(
				this.contractorAdvanceRequisitions, contractorAdvanceRequisitionRequest.contractorAdvanceRequisitions);
	}

	@Override
	public int hashCode() {
		return Objects.hash(requestInfo, contractorAdvanceRequisitions);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ContractorAdvanceRequisitionRequest {\n");

		sb.append("    requestInfo: ").append(toIndentedString(requestInfo)).append("\n");
		sb.append("    contractorAdvanceRequisitions: ").append(toIndentedString(contractorAdvanceRequisitions))
				.append("\n");
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
