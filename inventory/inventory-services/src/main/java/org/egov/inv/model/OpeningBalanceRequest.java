package org.egov.inv.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@ApiModel(description = "Hold the Opening Balance request information.")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-13T06:33:50.051Z")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OpeningBalanceRequest {
	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo = null;

	@JsonProperty("materialReceipt")
	private List<MaterialReceipt> materialReceipt = new ArrayList<MaterialReceipt>();

	public OpeningBalanceRequest requestInfo(RequestInfo requestInfo) {
		this.requestInfo = requestInfo;
		return this;
	}

	@ApiModelProperty(required = true, value = "")
	@NotNull
	@Valid
	public RequestInfo getRequestInfo() {
		return requestInfo;
	}

	public void setRequestInfo(RequestInfo requestInfo) {
		this.requestInfo = requestInfo;
	}

	public OpeningBalanceRequest materialReceipt(List<MaterialReceipt> materialReceipt) {
		this.materialReceipt = materialReceipt;
		return this;
	}

	public OpeningBalanceRequest addMaterialReceiptItem(MaterialReceipt materialReceiptItem) {
		this.materialReceipt.add(materialReceiptItem);
		return this;
	}

	@ApiModelProperty(required = true, value = "")
	@NotNull
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
		OpeningBalanceRequest openingBalanceRequest = (OpeningBalanceRequest) o;
		return Objects.equals(this.requestInfo, openingBalanceRequest.requestInfo)
				&& Objects.equals(this.materialReceipt, openingBalanceRequest.materialReceipt);
	}

	@Override
	public int hashCode() {
		return Objects.hash(requestInfo, materialReceipt);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class OpeningBalanceRequest {\n");
		sb.append("    requestInfo: ").append(toIndentedString(requestInfo)).append("\n");
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
