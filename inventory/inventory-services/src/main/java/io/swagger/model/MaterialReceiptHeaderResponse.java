package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.MaterialReceiptHeader;
import io.swagger.model.ResponseInfo;
import io.swagger.model.SupplierResponse.SupplierResponseBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Hold the Material Receipt Note Response information
 */
@ApiModel(description = "Hold the Material Receipt Note Response information")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-28T13:21:55.964+05:30")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MaterialReceiptHeaderResponse   {
  @JsonProperty("ResposneInfo")
  private org.egov.common.contract.response.ResponseInfo resposneInfo = null;

  @JsonProperty("MaterialReceipt")
  @Valid
  private List<MaterialReceiptHeader> materialReceipt = null;

  public MaterialReceiptHeaderResponse resposneInfo(org.egov.common.contract.response.ResponseInfo resposneInfo) {
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

  public MaterialReceiptHeaderResponse materialReceipt(List<MaterialReceiptHeader> materialReceipt) {
    this.materialReceipt = materialReceipt;
    return this;
  }

  public MaterialReceiptHeaderResponse addMaterialReceiptItem(MaterialReceiptHeader materialReceiptItem) {
    if (this.materialReceipt == null) {
      this.materialReceipt = new ArrayList<MaterialReceiptHeader>();
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

  public List<MaterialReceiptHeader> getMaterialReceipt() {
    return materialReceipt;
  }

  public void setMaterialReceipt(List<MaterialReceiptHeader> materialReceipt) {
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
    MaterialReceiptHeaderResponse materialReceiptHeaderResponse = (MaterialReceiptHeaderResponse) o;
    return Objects.equals(this.resposneInfo, materialReceiptHeaderResponse.resposneInfo) &&
        Objects.equals(this.materialReceipt, materialReceiptHeaderResponse.materialReceipt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(resposneInfo, materialReceipt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MaterialReceiptHeaderResponse {\n");
    
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

