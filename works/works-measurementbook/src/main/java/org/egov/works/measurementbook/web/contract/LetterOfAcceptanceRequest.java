package org.egov.works.measurementbook.web.contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Contract class to send response. Array of LetterOfAcceptance items are used
 * in case of search results, also multiple Letter Of Acceptance item is used
 * for create and update
 */
@ApiModel(description = "Contract class to send response. Array of LetterOfAcceptance items are used in case of search results, also multiple  Letter Of Acceptance item is used for create and update")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-21T10:42:18.195Z")

public class LetterOfAcceptanceRequest {
	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo = null;

	@JsonProperty("letterOfAcceptances")
	private List<LetterOfAcceptance> letterOfAcceptances = null;

	public LetterOfAcceptanceRequest requestInfo(RequestInfo requestInfo) {
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

	public LetterOfAcceptanceRequest letterOfAcceptances(List<LetterOfAcceptance> letterOfAcceptances) {
		this.letterOfAcceptances = letterOfAcceptances;
		return this;
	}

	public LetterOfAcceptanceRequest addLetterOfAcceptancesItem(LetterOfAcceptance letterOfAcceptancesItem) {
		if (this.letterOfAcceptances == null) {
			this.letterOfAcceptances = new ArrayList<LetterOfAcceptance>();
		}
		this.letterOfAcceptances.add(letterOfAcceptancesItem);
		return this;
	}

	/**
	 * Used for create and update only
	 * 
	 * @return letterOfAcceptances
	 **/
	@ApiModelProperty(value = "Used for create and update only")

	@Valid

	public List<LetterOfAcceptance> getLetterOfAcceptances() {
		return letterOfAcceptances;
	}

	public void setLetterOfAcceptances(List<LetterOfAcceptance> letterOfAcceptances) {
		this.letterOfAcceptances = letterOfAcceptances;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		LetterOfAcceptanceRequest letterOfAcceptanceRequest = (LetterOfAcceptanceRequest) o;
		return Objects.equals(this.requestInfo, letterOfAcceptanceRequest.requestInfo)
				&& Objects.equals(this.letterOfAcceptances, letterOfAcceptanceRequest.letterOfAcceptances);
	}

	@Override
	public int hashCode() {
		return Objects.hash(requestInfo, letterOfAcceptances);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class LetterOfAcceptanceRequest {\n");

		sb.append("    requestInfo: ").append(toIndentedString(requestInfo)).append("\n");
		sb.append("    letterOfAcceptances: ").append(toIndentedString(letterOfAcceptances)).append("\n");
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
