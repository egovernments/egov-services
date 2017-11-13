package org.egov.works.services.web.contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Contract class to send response. Array of Estimate Appropriation items are
 * used in case of search results, also multiple Estimate Appropriation item is
 * used for create and update
 */
@ApiModel(description = "Contract class to send response. Array of Estimate Appropriation items are used in case of search results, also multiple  Estimate Appropriation item is used for create and update")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-10T05:42:02.605Z")

public class EstimateAppropriationRequest {
	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo = null;

	@JsonProperty("estimateAppropriations")
	private List<EstimateAppropriation> estimateAppropriations = null;

	public EstimateAppropriationRequest requestInfo(RequestInfo requestInfo) {
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

	public EstimateAppropriationRequest estimateAppropriations(List<EstimateAppropriation> estimateAppropriations) {
		this.estimateAppropriations = estimateAppropriations;
		return this;
	}

	public EstimateAppropriationRequest addEstimateAppropriationsItem(
			EstimateAppropriation estimateAppropriationsItem) {
		if (this.estimateAppropriations == null) {
			this.estimateAppropriations = new ArrayList<EstimateAppropriation>();
		}
		this.estimateAppropriations.add(estimateAppropriationsItem);
		return this;
	}

	/**
	 * Used for create and update only
	 * 
	 * @return estimateAppropriations
	 **/
	@ApiModelProperty(value = "Used for create and update only")

	@Valid

	public List<EstimateAppropriation> getEstimateAppropriations() {
		return estimateAppropriations;
	}

	public void setEstimateAppropriations(List<EstimateAppropriation> estimateAppropriations) {
		this.estimateAppropriations = estimateAppropriations;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		EstimateAppropriationRequest estimateAppropriationRequest = (EstimateAppropriationRequest) o;
		return Objects.equals(this.requestInfo, estimateAppropriationRequest.requestInfo)
				&& Objects.equals(this.estimateAppropriations, estimateAppropriationRequest.estimateAppropriations);
	}

	@Override
	public int hashCode() {
		return Objects.hash(requestInfo, estimateAppropriations);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class EstimateAppropriationRequest {\n");

		sb.append("    requestInfo: ").append(toIndentedString(requestInfo)).append("\n");
		sb.append("    estimateAppropriations: ").append(toIndentedString(estimateAppropriations)).append("\n");
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
