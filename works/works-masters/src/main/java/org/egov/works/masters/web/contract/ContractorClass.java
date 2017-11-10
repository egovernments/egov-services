package org.egov.works.masters.web.contract;

import java.math.BigDecimal;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * An Object that holds Contractor Class details
 */
@ApiModel(description = "An Object that holds Contractor Class details")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-10T10:39:50.702Z")

public class ContractorClass   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("class")
  private String propertyClass = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("minAmount")
  private BigDecimal minAmount = null;

  @JsonProperty("maxAmount")
  private BigDecimal maxAmount = null;

  public ContractorClass id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Contractor Class.
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Contractor Class.")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public ContractorClass tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the Contractor Class.
   * @return tenantId
  **/
  @ApiModelProperty(required = true, value = "Tenant id of the Contractor Class.")
  @NotNull

 @Size(min=2,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public ContractorClass propertyClass(String propertyClass) {
    this.propertyClass = propertyClass;
    return this;
  }

   /**
   * Class of the Contractor
   * @return propertyClass
  **/
  @ApiModelProperty(required = true, value = "Class of the Contractor")
  @NotNull

 @Size(min=1,max=100)
  public String getPropertyClass() {
    return propertyClass;
  }

  public void setPropertyClass(String propertyClass) {
    this.propertyClass = propertyClass;
  }

  public ContractorClass description(String description) {
    this.description = description;
    return this;
  }

   /**
   * Description of the Contractor Class
   * @return description
  **/
  @ApiModelProperty(required = true, value = "Description of the Contractor Class")
  @NotNull

 @Pattern(regexp="[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]+") @Size(min=1,max=1024)
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public ContractorClass minAmount(BigDecimal minAmount) {
    this.minAmount = minAmount;
    return this;
  }

   /**
   * Minimum Amount of the Contractor Class.
   * @return minAmount
  **/
  @ApiModelProperty(required = true, value = "Minimum Amount of the Contractor Class.")
  @NotNull

  @Valid

  public BigDecimal getMinAmount() {
    return minAmount;
  }

  public void setMinAmount(BigDecimal minAmount) {
    this.minAmount = minAmount;
  }

  public ContractorClass maxAmount(BigDecimal maxAmount) {
    this.maxAmount = maxAmount;
    return this;
  }

   /**
   * Maximum Amount of the Contractor Class.
   * @return maxAmount
  **/
  @ApiModelProperty(required = true, value = "Maximum Amount of the Contractor Class.")
  @NotNull

  @Valid

  public BigDecimal getMaxAmount() {
    return maxAmount;
  }

  public void setMaxAmount(BigDecimal maxAmount) {
    this.maxAmount = maxAmount;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ContractorClass contractorClass = (ContractorClass) o;
    return Objects.equals(this.id, contractorClass.id) &&
        Objects.equals(this.tenantId, contractorClass.tenantId) &&
        Objects.equals(this.propertyClass, contractorClass.propertyClass) &&
        Objects.equals(this.description, contractorClass.description) &&
        Objects.equals(this.minAmount, contractorClass.minAmount) &&
        Objects.equals(this.maxAmount, contractorClass.maxAmount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, propertyClass, description, minAmount, maxAmount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ContractorClass {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    propertyClass: ").append(toIndentedString(propertyClass)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    minAmount: ").append(toIndentedString(minAmount)).append("\n");
    sb.append("    maxAmount: ").append(toIndentedString(maxAmount)).append("\n");
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

