package org.egov.works.services.web.contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Contract class to send response. Array of Document Detail items are used in
 * case of search results, also multiple Document Detail item is used for create
 * and update
 */
@ApiModel(description = "Contract class to send response. Array of Document Detail items are used in case of search results, also multiple  Document Detail item is used for create and update")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-10T05:42:02.605Z")

public class DocumentDetailResponse {
	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo = null;

	@JsonProperty("documentDetails")
	private List<DocumentDetail> documentDetails = null;

	public DocumentDetailResponse responseInfo(ResponseInfo responseInfo) {
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

	public DocumentDetailResponse documentDetails(List<DocumentDetail> documentDetails) {
		this.documentDetails = documentDetails;
		return this;
	}

	public DocumentDetailResponse addDocumentDetailsItem(DocumentDetail documentDetailsItem) {
		if (this.documentDetails == null) {
			this.documentDetails = new ArrayList<DocumentDetail>();
		}
		this.documentDetails.add(documentDetailsItem);
		return this;
	}

	/**
	 * Used for search result and create only
	 * 
	 * @return documentDetails
	 **/
	@ApiModelProperty(value = "Used for search result and create only")

	@Valid

	public List<DocumentDetail> getDocumentDetails() {
		return documentDetails;
	}

	public void setDocumentDetails(List<DocumentDetail> documentDetails) {
		this.documentDetails = documentDetails;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		DocumentDetailResponse documentDetailResponse = (DocumentDetailResponse) o;
		return Objects.equals(this.responseInfo, documentDetailResponse.responseInfo)
				&& Objects.equals(this.documentDetails, documentDetailResponse.documentDetails);
	}

	@Override
	public int hashCode() {
		return Objects.hash(responseInfo, documentDetails);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class DocumentDetailResponse {\n");

		sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
		sb.append("    documentDetails: ").append(toIndentedString(documentDetails)).append("\n");
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
