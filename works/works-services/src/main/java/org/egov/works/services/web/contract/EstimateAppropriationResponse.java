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

public class EstimateAppropriationResponse {
	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo = null;

	@JsonProperty("estimateAppropriations")
	private List<EstimateAppropriation> estimateAppropriations = null;

	public EstimateAppropriationResponse responseInfo(ResponseInfo responseInfo) {
		this.responseInfo = responseInfo;
		return this;
	}

	/**
	 * Get responseInfo
	 * 
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

	public EstimateAppropriationResponse estimateAppropriations(List<EstimateAppropriation> estimateAppropriations) {
		this.estimateAppropriations = estimateAppropriations;
		return this;
	}

	public EstimateAppropriationResponse addEstimateAppropriationsItem(
			EstimateAppropriation estimateAppropriationsItem) {
		if (this.estimateAppropriations == null) {
			this.estimateAppropriations = new ArrayList<EstimateAppropriation>();
		}
		this.estimateAppropriations.add(estimateAppropriationsItem);
		return this;
	}

	/**
	 * Used for search result and create only
	 * 
	 * @return estimateAppropriations
	 **/
	@ApiModelProperty(value = "Used for search result and create only")

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
		EstimateAppropriationResponse estimateAppropriationResponse = (EstimateAppropriationResponse) o;
		return Objects.equals(this.responseInfo, estimateAppropriationResponse.responseInfo)
				&& Objects.equals(this.estimateAppropriations, estimateAppropriationResponse.estimateAppropriations);
	}

	@Override
	public int hashCode() {
		return Objects.hash(responseInfo, estimateAppropriations);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class EstimateAppropriationResponse {\n");

		sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
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
