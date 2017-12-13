package org.egov.inv.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@ApiModel(description = "Hold the Opening Balance Response information")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-13T06:33:50.051Z")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OpeningBalanceResponse {
	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo = null;

	@JsonProperty("materialReceipt")
	private List<MaterialReceipt> materialReceipt = null;

	public OpeningBalanceResponse responseInfo(ResponseInfo responseInfo) {
		this.responseInfo = responseInfo;
		return this;
	}

	@ApiModelProperty(value = "")
	@Valid
	public ResponseInfo getResponseInfo() {
		return responseInfo;
	}

	public void setResponseInfo(ResponseInfo responseInfo) {
		this.responseInfo = responseInfo;
	}

	public OpeningBalanceResponse materialReceipt(List<MaterialReceipt> materialReceipt) {
		this.materialReceipt = materialReceipt;
		return this;
	}

	public OpeningBalanceResponse addMaterialReceiptItem(MaterialReceipt materialReceiptItem) {
		if (this.materialReceipt == null) {
			this.materialReceipt = new ArrayList<MaterialReceipt>();
		}
		this.materialReceipt.add(materialReceiptItem);
		return this;
	}

	@ApiModelProperty(value = "")
	@Valid
	public List<MaterialReceipt> getMaterialReceipt() {
		return materialReceipt;
	}

	public void setMaterialReceipt(List<MaterialReceipt> materialReceipt) {
		this.materialReceipt = materialReceipt;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		OpeningBalanceResponse openingBalanceResponse = (OpeningBalanceResponse) o;
		return Objects.equals(this.responseInfo, openingBalanceResponse.responseInfo)
				&& Objects.equals(this.materialReceipt, openingBalanceResponse.materialReceipt);
	}

	@Override
	public int hashCode() {
		return Objects.hash(responseInfo, materialReceipt);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class OpeningBalanceResponse {\n");
		sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
		sb.append("    materialReceipt: ").append(toIndentedString(materialReceipt)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	private String toIndentedString(java.lang.Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}
