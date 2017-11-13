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
 * Hold the Material Receipt Note Response information
 */
@ApiModel(description = "Hold the Material Receipt Note Response information")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-08T06:17:26.594Z")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MaterialReceiptResponse   {
  @JsonProperty("ResposneInfo")
  private org.egov.common.contract.response.ResponseInfo resposneInfo = null;

  @JsonProperty("MaterialReceipt")
  private List<MaterialReceipt> materialReceipt = null;

  public MaterialReceiptResponse resposneInfo(org.egov.common.contract.response.ResponseInfo resposneInfo) {
    this.resposneInfo = resposneInfo;
    return this;
  }

   /**
   * Get resposneInfo
   * @return resposneInfo
  **/
  @ApiModelProperty(value = "")

  @Valid

  public org.egov.common.contract.response.ResponseInfo getResposneInfo() {
    return resposneInfo;
  }

  public void setResponseInfo(org.egov.common.contract.response.ResponseInfo resposneInfo) {
    this.resposneInfo = resposneInfo;
  }

  public MaterialReceiptResponse materialReceipt(List<MaterialReceipt> materialReceipt) {
    this.materialReceipt = materialReceipt;
    return this;
  }

  public MaterialReceiptResponse addMaterialReceiptItem(MaterialReceipt materialReceiptItem) {
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
    MaterialReceiptResponse materialReceiptResponse = (MaterialReceiptResponse) o;
    return Objects.equals(this.resposneInfo, materialReceiptResponse.resposneInfo) &&
        Objects.equals(this.materialReceipt, materialReceiptResponse.materialReceipt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(resposneInfo, materialReceipt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MaterialReceiptResponse {\n");
    
    sb.append("    resposneInfo: ").append(toIndentedString(resposneInfo)).append("\n");
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

