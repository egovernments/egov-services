package org.egov.works.workorder.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * BillStatus
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-01-16T09:56:01.690Z")

public class BillStatus   {
  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("code")
  private String code = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("moduleType")
  private String moduleType = null;

  @JsonProperty("orderNumber")
  private Long orderNumber = null;

  @JsonProperty("description")
  private String description = null;

  public BillStatus tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * tenantId Unique Identifier of the tenant, Like AP, AP.Kurnool etc. represents the client for which the transaction is created. 
   * @return tenantId
  **/
  @ApiModelProperty(required = true, value = "tenantId Unique Identifier of the tenant, Like AP, AP.Kurnool etc. represents the client for which the transaction is created. ")
  @NotNull

 @Size(min=0,max=256)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public BillStatus code(String code) {
    this.code = code;
    return this;
  }

   /**
   * code is the unique identifier 
   * @return code
  **/
  @ApiModelProperty(required = true, value = "code is the unique identifier ")
  @NotNull

 @Size(min=1,max=50)
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public BillStatus name(String name) {
    this.name = name;
    return this;
  }

   /**
   * name of the bill status 
   * @return name
  **/
  @ApiModelProperty(required = true, value = "name of the bill status ")
  @NotNull

 @Size(min=1,max=50)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BillStatus moduleType(String moduleType) {
    this.moduleType = moduleType;
    return this;
  }

   /**
   * moduleType of bill status 
   * @return moduleType
  **/
  @ApiModelProperty(required = true, value = "moduleType of bill status ")
  @NotNull

 @Size(min=1,max=50)
  public String getModuleType() {
    return moduleType;
  }

  public void setModuleType(String moduleType) {
    this.moduleType = moduleType;
  }

  public BillStatus orderNumber(Long orderNumber) {
    this.orderNumber = orderNumber;
    return this;
  }

   /**
   * order Number of bill status 
   * @return orderNumber
  **/
  @ApiModelProperty(value = "order Number of bill status ")


  public Long getOrderNumber() {
    return orderNumber;
  }

  public void setOrderNumber(Long orderNumber) {
    this.orderNumber = orderNumber;
  }

  public BillStatus description(String description) {
    this.description = description;
    return this;
  }

   /**
   * description of bill status 
   * @return description
  **/
  @ApiModelProperty(value = "description of bill status ")

 @Size(min=0,max=250)
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BillStatus billStatus = (BillStatus) o;
    return Objects.equals(this.tenantId, billStatus.tenantId) &&
        Objects.equals(this.code, billStatus.code) &&
        Objects.equals(this.name, billStatus.name) &&
        Objects.equals(this.moduleType, billStatus.moduleType) &&
        Objects.equals(this.orderNumber, billStatus.orderNumber) &&
        Objects.equals(this.description, billStatus.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tenantId, code, name, moduleType, orderNumber, description);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BillStatus {\n");

    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    moduleType: ").append(toIndentedString(moduleType)).append("\n");
    sb.append("    orderNumber: ").append(toIndentedString(orderNumber)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
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

