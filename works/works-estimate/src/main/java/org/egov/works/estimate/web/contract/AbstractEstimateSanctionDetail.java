package org.egov.works.estimate.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * An Object that holds Sanction Details of Abstract Estimate
 */
@ApiModel(description = "An Object that holds Sanction Details of Abstract Estimate")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-29T09:03:53.949Z")

public class AbstractEstimateSanctionDetail   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("abstractEstimate")
  private String abstractEstimate = null;

  @JsonProperty("sanctionType")
  private EstimateSanctionType sanctionType = null;

  @JsonProperty("sanctionAuthority")
  private EstimateSanctionAuthority sanctionAuthority = null;

  @JsonProperty("deleted")
  private Boolean deleted = false;

  public AbstractEstimateSanctionDetail id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Sanction Details of Abstract Estimate
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Sanction Details of Abstract Estimate")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public AbstractEstimateSanctionDetail tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the Sanction Details of Abstract Estimate
   * @return tenantId
  **/
  @ApiModelProperty(required = true, value = "Tenant id of the Sanction Details of Abstract Estimate")
  @NotNull

 @Size(min=2,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public AbstractEstimateSanctionDetail abstractEstimate(String abstractEstimate) {
    this.abstractEstimate = abstractEstimate;
    return this;
  }

   /**
   * Reference to Sanction Details of Abstract Estimate Object
   * @return abstractEstimate
  **/
  @ApiModelProperty(required = true, value = "Reference to Sanction Details of Abstract Estimate Object")
  //@NotNull


  public String getAbstractEstimate() {
    return abstractEstimate;
  }

  public void setAbstractEstimate(String abstractEstimate) {
    this.abstractEstimate = abstractEstimate;
  }

  public AbstractEstimateSanctionDetail sanctionType(EstimateSanctionType sanctionType) {
    this.sanctionType = sanctionType;
    return this;
  }

   /**
   * Reference to Estimate Sanction Type enum
   * @return sanctionType
  **/
  @ApiModelProperty(required = true, value = "Reference to Estimate Sanction Type enum")
  @NotNull

  @Valid

  public EstimateSanctionType getSanctionType() {
    return sanctionType;
  }

  public void setSanctionType(EstimateSanctionType sanctionType) {
    this.sanctionType = sanctionType;
  }

  public AbstractEstimateSanctionDetail sanctionAuthority(EstimateSanctionAuthority sanctionAuthority) {
    this.sanctionAuthority = sanctionAuthority;
    return this;
  }

   /**
   * Reference to Estimate Sanction Authority object
   * @return sanctionAuthority
  **/
  @ApiModelProperty(required = true, value = "Reference to Estimate Sanction Authority object")
  @NotNull

  @Valid

  public EstimateSanctionAuthority getSanctionAuthority() {
    return sanctionAuthority;
  }

  public void setSanctionAuthority(EstimateSanctionAuthority sanctionAuthority) {
    this.sanctionAuthority = sanctionAuthority;
  }

  public AbstractEstimateSanctionDetail deleted(Boolean deleted) {
    this.deleted = deleted;
    return this;
  }

   /**
   * Boolean value to identify whether the object is deleted or not from UI.
   * @return deleted
  **/
  @ApiModelProperty(value = "Boolean value to identify whether the object is deleted or not from UI.")


  public Boolean getDeleted() {
    return deleted;
  }

  public void setDeleted(Boolean deleted) {
    this.deleted = deleted;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AbstractEstimateSanctionDetail abstractEstimateSanctionDetail = (AbstractEstimateSanctionDetail) o;
    return Objects.equals(this.id, abstractEstimateSanctionDetail.id) &&
        Objects.equals(this.tenantId, abstractEstimateSanctionDetail.tenantId) &&
        Objects.equals(this.abstractEstimate, abstractEstimateSanctionDetail.abstractEstimate) &&
        Objects.equals(this.sanctionType, abstractEstimateSanctionDetail.sanctionType) &&
        Objects.equals(this.sanctionAuthority, abstractEstimateSanctionDetail.sanctionAuthority) &&
        Objects.equals(this.deleted, abstractEstimateSanctionDetail.deleted);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, abstractEstimate, sanctionType, sanctionAuthority, deleted);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AbstractEstimateSanctionDetail {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    abstractEstimate: ").append(toIndentedString(abstractEstimate)).append("\n");
    sb.append("    sanctionType: ").append(toIndentedString(sanctionType)).append("\n");
    sb.append("    sanctionAuthority: ").append(toIndentedString(sanctionAuthority)).append("\n");
    sb.append("    deleted: ").append(toIndentedString(deleted)).append("\n");
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

