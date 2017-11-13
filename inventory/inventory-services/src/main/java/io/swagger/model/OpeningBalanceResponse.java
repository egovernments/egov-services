package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.MaterialReceipt;
import io.swagger.model.ResponseInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Hold the Opening Balance Response information
 */
@ApiModel(description = "Hold the Opening Balance Response information")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-13T06:33:50.051Z")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OpeningBalanceResponse   {
  @JsonProperty("ResponseInfo")
  private org.egov.common.contract.response.ResponseInfo responseInfo = null;

  @JsonProperty("materialReceipt")
  private List<MaterialReceipt> materialReceipt = null;

  public OpeningBalanceResponse responseInfo(org.egov.common.contract.response.ResponseInfo responseInfo) {
    this.responseInfo = responseInfo;
    return this;
  }

   /**
   * Get responseInfo
   * @return responseInfo
  **/
  @ApiModelProperty(value = "")

  @Valid

  public org.egov.common.contract.response.ResponseInfo getResponseInfo() {
    return responseInfo;
  }

  public void setResponseInfo(org.egov.common.contract.response.ResponseInfo responseInfo) {
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

   /**
   * Get materialReceipt
   * @return materialReceipt
  **/
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
    return Objects.equals(this.responseInfo, openingBalanceResponse.responseInfo) &&
        Objects.equals(this.materialReceipt, openingBalanceResponse.materialReceipt);
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

