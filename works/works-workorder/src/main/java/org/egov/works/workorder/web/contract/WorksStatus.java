package org.egov.works.workorder.web.contract;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * An Object that holds Status of all Works Module
 */
@ApiModel(description = "An Object that holds Status of all Works Module")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-12-26T08:14:22.308Z")

public class WorksStatus   {
  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("code")
  private String code = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("moduleType")
  private String moduleType = null;

  @JsonProperty("orderNumber")
  private BigDecimal orderNumber = null;

  public WorksStatus tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the Works Status
   * @return tenantId
  **/
  @ApiModelProperty(required = true, value = "Tenant id of the Works Status")
  //@NotNull

 @Size(min=2,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public WorksStatus code(String code) {
    this.code = code;
    return this;
  }

   /**
   * Status Code
   * @return code
  **/
  @ApiModelProperty(required = true, value = "Status Code")
  @NotNull

 @Pattern(regexp="[a-zA-Z0-9-_\\\\]+") @Size(min=1,max=100)
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public WorksStatus description(String description) {
    this.description = description;
    return this;
  }

   /**
   * description about the Status
   * @return description
  **/
  @ApiModelProperty(value = "description about the Status")

 @Size(max=256)
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public WorksStatus moduleType(String moduleType) {
    this.moduleType = moduleType;
    return this;
  }

   /**
   * Type of Module
   * @return moduleType
  **/
  @ApiModelProperty(required = true, value = "Type of Module")
  //@NotNull

 @Pattern(regexp="[a-zA-Z0-9-_\\\\]+") @Size(min=1,max=100)
  public String getModuleType() {
    return moduleType;
  }

  public void setModuleType(String moduleType) {
    this.moduleType = moduleType;
  }

  public WorksStatus orderNumber(BigDecimal orderNumber) {
    this.orderNumber = orderNumber;
    return this;
  }

   /**
   * order Number for the status
   * @return orderNumber
  **/
  @ApiModelProperty(value = "order Number for the status")

  @Valid

  public BigDecimal getOrderNumber() {
    return orderNumber;
  }

  public void setOrderNumber(BigDecimal orderNumber) {
    this.orderNumber = orderNumber;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WorksStatus worksStatus = (WorksStatus) o;
    return Objects.equals(this.tenantId, worksStatus.tenantId) &&
        Objects.equals(this.code, worksStatus.code) &&
        Objects.equals(this.description, worksStatus.description) &&
        Objects.equals(this.moduleType, worksStatus.moduleType) &&
        Objects.equals(this.orderNumber, worksStatus.orderNumber);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tenantId, code, description, moduleType, orderNumber);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WorksStatus {\n");
    
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    moduleType: ").append(toIndentedString(moduleType)).append("\n");
    sb.append("    orderNumber: ").append(toIndentedString(orderNumber)).append("\n");
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

