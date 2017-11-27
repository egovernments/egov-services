package org.egov.works.measurementbook.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Contract class to send response. Array of Letter Of Acceptance items are used
 * in case of search results, also multiple Letter Of Acceptance item is used
 * for create and update
 */
@ApiModel(description = "Contract class to send response. Array of Letter Of Acceptance items are used in case of search results, also multiple  Letter Of Acceptance item is used for create and update")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-21T10:42:18.195Z")

public class LetterOfAcceptanceResponse {
	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo = null;

	@JsonProperty("letterOfAcceptances")
	private List<LetterOfAcceptance> letterOfAcceptances = null;

	public LetterOfAcceptanceResponse responseInfo(ResponseInfo responseInfo) {
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

	public LetterOfAcceptanceResponse letterOfAcceptances(List<LetterOfAcceptance> letterOfAcceptances) {
		this.letterOfAcceptances = letterOfAcceptances;
		return this;
	}

	public LetterOfAcceptanceResponse addLetterOfAcceptancesItem(LetterOfAcceptance letterOfAcceptancesItem) {
		if (this.letterOfAcceptances == null) {
			this.letterOfAcceptances = new ArrayList<LetterOfAcceptance>();
		}
		this.letterOfAcceptances.add(letterOfAcceptancesItem);
		return this;
	}

	/**
	 * Used for search result and create only
	 * 
	 * @return letterOfAcceptances
	 **/
	@ApiModelProperty(value = "Used for search result and create only")

	@Valid

	public List<LetterOfAcceptance> getLetterOfAcceptances() {
		return letterOfAcceptances;
	}

	public void setLetterOfAcceptances(List<LetterOfAcceptance> letterOfAcceptances) {
		this.letterOfAcceptances = letterOfAcceptances;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		LetterOfAcceptanceResponse letterOfAcceptanceResponse = (LetterOfAcceptanceResponse) o;
		return Objects.equals(this.responseInfo, letterOfAcceptanceResponse.responseInfo)
				&& Objects.equals(this.letterOfAcceptances, letterOfAcceptanceResponse.letterOfAcceptances);
	}

	@Override
	public int hashCode() {
		return Objects.hash(responseInfo, letterOfAcceptances);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class LetterOfAcceptanceResponse {\n");

		sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
		sb.append("    letterOfAcceptances: ").append(toIndentedString(letterOfAcceptances)).append("\n");
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
