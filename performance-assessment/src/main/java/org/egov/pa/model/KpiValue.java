package org.egov.pa.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Object which holds the mapping for KPI and its actual values entered by ULBs
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-30T06:35:11.343Z")

public class KpiValue   {
  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("kpiCode")
  private String kpiCode = null;

  @JsonProperty("actualValue")
  private Long actualValue = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("documents")
  private List<Document> documents = null;

  @JsonProperty("createdBy")
  private Long createdBy = null;

  @JsonProperty("lastModifiedBy")
  private Long lastModifiedBy = null;

  @JsonProperty("createdDate")
  private Long createdDate = null;

  @JsonProperty("lastModifiedDate")
  private Long lastModifiedDate = null;

  public KpiValue id(Long id) {
    this.id = id;
    return this;
  }

   /**
   * unique identifier of a KPI Value object
   * @return id
  **/


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public KpiValue kpiCode(String kpiCode) {
    this.kpiCode = kpiCode;
    return this;
  }

   /**
   * Unique Code given to the KPI
   * @return kpiCode
  **/
  @NotNull

 @Pattern(regexp="^[a-zA-Z0-9]+$")
  public String getKpiCode() {
    return kpiCode;
  }

  public void setKpiCode(String kpiCode) {
    this.kpiCode = kpiCode;
  }

  public KpiValue actualValue(Long actualValue) {
    this.actualValue = actualValue;
    return this;
  }

   /**
   * Actual Value at ULB Level for the KPI Target
   * @return actualValue
  **/
  @NotNull


  public Long getActualValue() {
    return actualValue;
  }

  public void setActualValue(Long actualValue) {
    this.actualValue = actualValue;
  }

  public KpiValue tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Unique Identifier of the tenant
   * @return tenantId
  **/
  @NotNull

 @Pattern(regexp="^[a-zA-Z0-9.]+$")
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public KpiValue documents(List<Document> documents) {
    this.documents = documents;
    return this;
  }

  public KpiValue addDocumentsItem(Document documentsItem) {
    if (this.documents == null) {
      this.documents = new ArrayList<Document>();
    }
    this.documents.add(documentsItem);
    return this;
  }

   /**
   * Get documents
   * @return documents
  **/

  @Valid

  public List<Document> getDocuments() {
    return documents;
  }

  public void setDocuments(List<Document> documents) {
    this.documents = documents;
  }

  public KpiValue createdBy(Long createdBy) {
    this.createdBy = createdBy;
    return this;
  }

   /**
   * User ID of the User who created the record
   * @return createdBy
  **/


  public Long getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(Long createdBy) {
    this.createdBy = createdBy;
  }

  public KpiValue lastModifiedBy(Long lastModifiedBy) {
    this.lastModifiedBy = lastModifiedBy;
    return this;
  }

   /**
   * User ID of the User who last updated the record
   * @return lastModifiedBy
  **/


  public Long getLastModifiedBy() {
    return lastModifiedBy;
  }

  public void setLastModifiedBy(Long lastModifiedBy) {
    this.lastModifiedBy = lastModifiedBy;
  }

  public KpiValue createdDate(Long createdDate) {
    this.createdDate = createdDate;
    return this;
  }

   /**
   * Date on which the record got created
   * @return createdDate
  **/


  public Long getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Long createdDate) {
    this.createdDate = createdDate;
  }

  public KpiValue lastModifiedDate(Long lastModifiedDate) {
    this.lastModifiedDate = lastModifiedDate;
    return this;
  }

   /**
   * Date on which the record for modified
   * @return lastModifiedDate
  **/


  public Long getLastModifiedDate() {
    return lastModifiedDate;
  }

  public void setLastModifiedDate(Long lastModifiedDate) {
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
    KpiValue kpiValue = (KpiValue) o;
    return Objects.equals(this.id, kpiValue.id) &&
        Objects.equals(this.kpiCode, kpiValue.kpiCode) &&
        Objects.equals(this.actualValue, kpiValue.actualValue) &&
        Objects.equals(this.tenantId, kpiValue.tenantId) &&
        Objects.equals(this.documents, kpiValue.documents) &&
        Objects.equals(this.createdBy, kpiValue.createdBy) &&
        Objects.equals(this.lastModifiedBy, kpiValue.lastModifiedBy) &&
        Objects.equals(this.createdDate, kpiValue.createdDate) &&
        Objects.equals(this.lastModifiedDate, kpiValue.lastModifiedDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, kpiCode, actualValue, tenantId, documents, createdBy, lastModifiedBy, createdDate, lastModifiedDate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class KpiValue {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    kpiCode: ").append(toIndentedString(kpiCode)).append("\n");
    sb.append("    actualValue: ").append(toIndentedString(actualValue)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    documents: ").append(toIndentedString(documents)).append("\n");
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

