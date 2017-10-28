package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.MaterialReceiptHeader;
import io.swagger.model.RequestInfo;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Hold the Material Receipt Note request information.
 */
@ApiModel(description = "Hold the Material Receipt Note request information.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-28T13:21:55.964+05:30")

public class MaterialReceiptHeaderRequest   {
  @JsonProperty("ResposneInfo")
  private RequestInfo resposneInfo = null;

  @JsonProperty("MaterialReceipt")
  @Valid
  private List<MaterialReceiptHeader> materialReceipt = null;

  public MaterialReceiptHeaderRequest resposneInfo(RequestInfo resposneInfo) {
    this.resposneInfo = resposneInfo;
    return this;
  }

   /**
   * Get resposneInfo
   * @return resposneInfo
  **/
  @ApiModelProperty(value = "")

  @Valid

  public RequestInfo getResposneInfo() {
    return resposneInfo;
  }

  public void setResposneInfo(RequestInfo resposneInfo) {
    this.resposneInfo = resposneInfo;
  }

  public MaterialReceiptHeaderRequest materialReceipt(List<MaterialReceiptHeader> materialReceipt) {
    this.materialReceipt = materialReceipt;
    return this;
  }

  public MaterialReceiptHeaderRequest addMaterialReceiptItem(MaterialReceiptHeader materialReceiptItem) {
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
    MaterialReceiptHeaderRequest materialReceiptHeaderRequest = (MaterialReceiptHeaderRequest) o;
    return Objects.equals(this.resposneInfo, materialReceiptHeaderRequest.resposneInfo) &&
        Objects.equals(this.materialReceipt, materialReceiptHeaderRequest.materialReceipt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(resposneInfo, materialReceipt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MaterialReceiptHeaderRequest {\n");
    
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

