package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.RequestInfo;
import io.swagger.model.Supplier;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Contract class for web request. Array of Supplier items  are used in case of create or update
 */
@ApiModel(description = "Contract class for web request. Array of Supplier items  are used in case of create or update")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-28T13:21:55.964+05:30")

public class SupplierRequest   {
  @JsonProperty("requestInfo")
  private RequestInfo requestInfo = null;

  @JsonProperty("suppliers")
  @Valid
  private List<Supplier> suppliers = null;

  public SupplierRequest requestInfo(RequestInfo requestInfo) {
    this.requestInfo = requestInfo;
    return this;
  }

   /**
   * Get requestInfo
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

  public SupplierRequest suppliers(List<Supplier> suppliers) {
    this.suppliers = suppliers;
    return this;
  }

  public SupplierRequest addSuppliersItem(Supplier suppliersItem) {
    if (this.suppliers == null) {
      this.suppliers = new ArrayList<Supplier>();
    }
    this.suppliers.add(suppliersItem);
    return this;
  }

   /**
   * Used for search result and create only
   * @return suppliers
  **/
  @ApiModelProperty(value = "Used for search result and create only")

  @Valid

  public List<Supplier> getSuppliers() {
    return suppliers;
  }

  public void setSuppliers(List<Supplier> suppliers) {
    this.suppliers = suppliers;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SupplierRequest supplierRequest = (SupplierRequest) o;
    return Objects.equals(this.requestInfo, supplierRequest.requestInfo) &&
        Objects.equals(this.suppliers, supplierRequest.suppliers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestInfo, suppliers);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SupplierRequest {\n");
    
    sb.append("    requestInfo: ").append(toIndentedString(requestInfo)).append("\n");
    sb.append("    suppliers: ").append(toIndentedString(suppliers)).append("\n");
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

