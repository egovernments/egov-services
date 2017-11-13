package org.egov.works.masters.web.contract;

import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * Auditable
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-10T10:39:50.702Z")

public class Auditable   {
  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("createdBy")
  private User createdBy = null;

  @JsonProperty("lastModifiedBy")
  private User lastModifiedBy = null;

  @JsonProperty("createdDate")
  private LocalDate createdDate = null;

  @JsonProperty("lastModifiedDate")
  private LocalDate lastModifiedDate = null;

  public Auditable tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * tenantId Unique Identifier of the tenant, Like AP, AP.Kurnool etc. represents the client for which the transaction is created. 
   * @return tenantId
  **/
  @ApiModelProperty(value = "tenantId Unique Identifier of the tenant, Like AP, AP.Kurnool etc. represents the client for which the transaction is created. ")

 @Size(min=5,max=50)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public Auditable createdBy(User createdBy) {
    this.createdBy = createdBy;
    return this;
  }

   /**
   * createdBy is the logged in user who is conducting transaction 
   * @return createdBy
  **/
  @ApiModelProperty(value = "createdBy is the logged in user who is conducting transaction ")

  @Valid

  public User getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(User createdBy) {
    this.createdBy = createdBy;
  }

  public Auditable lastModifiedBy(User lastModifiedBy) {
    this.lastModifiedBy = lastModifiedBy;
    return this;
  }

   /**
   * lastModifiedBy is the logged in user who is updating transaction 
   * @return lastModifiedBy
  **/
  @ApiModelProperty(value = "lastModifiedBy is the logged in user who is updating transaction ")

  @Valid

  public User getLastModifiedBy() {
    return lastModifiedBy;
  }

  public void setLastModifiedBy(User lastModifiedBy) {
    this.lastModifiedBy = lastModifiedBy;
  }

  public Auditable createdDate(LocalDate createdDate) {
    this.createdDate = createdDate;
    return this;
  }

   /**
   * 
   * @return createdDate
  **/
  @ApiModelProperty(value = "")

  @Valid

  public LocalDate getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(LocalDate createdDate) {
    this.createdDate = createdDate;
  }

  public Auditable lastModifiedDate(LocalDate lastModifiedDate) {
    this.lastModifiedDate = lastModifiedDate;
    return this;
  }

   /**
   * lastModifiedDate date is on which trnasaction is updated lastly 
   * @return lastModifiedDate
  **/
  @ApiModelProperty(value = "lastModifiedDate date is on which trnasaction is updated lastly ")

  @Valid

  public LocalDate getLastModifiedDate() {
    return lastModifiedDate;
  }

  public void setLastModifiedDate(LocalDate lastModifiedDate) {
    this.lastModifiedDate = lastModifiedDate;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Auditable auditable = (Auditable) o;
    return Objects.equals(this.tenantId, auditable.tenantId) &&
        Objects.equals(this.createdBy, auditable.createdBy) &&
        Objects.equals(this.lastModifiedBy, auditable.lastModifiedBy) &&
        Objects.equals(this.createdDate, auditable.createdDate) &&
        Objects.equals(this.lastModifiedDate, auditable.lastModifiedDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tenantId, createdBy, lastModifiedBy, createdDate, lastModifiedDate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Auditable {\n");
    
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    createdBy: ").append(toIndentedString(createdBy)).append("\n");
    sb.append("    lastModifiedBy: ").append(toIndentedString(lastModifiedBy)).append("\n");
    sb.append("    createdDate: ").append(toIndentedString(createdDate)).append("\n");
    sb.append("    lastModifiedDate: ").append(toIndentedString(lastModifiedDate)).append("\n");
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

