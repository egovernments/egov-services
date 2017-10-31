package org.egov.pa.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This is the object for KPI Details not specific to any ULB
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-30T06:35:11.343Z")

public class KPI   {
  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("code")
  private String code = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("kpiTarget")
  private KpiTarget kpiTarget = null; 

  @JsonProperty("finYear")
  private String finYear = null;

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

  public KPI id(Long id) {
    this.id = id;
    return this;
  }

   /**
   * unique id of the KPI
   * @return id
  **/


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public KPI name(String name) {
    this.name = name;
    return this;
  }

   /**
   * Name of the KPI
   * @return name
  **/
  @NotNull

 @Pattern(regexp="^[a-zA-Z0-9 ]+$")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public KPI code(String code) {
    this.code = code;
    return this;
  }

   /**
   * Unique Code given to the KPI
   * @return code
  **/
  @NotNull

 @Pattern(regexp="^[a-zA-Z0-9]+$")
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public KPI description(String description) {
    this.description = description;
    return this;
  }

   /**
   * Description for the KPI
   * @return description
  **/

 @Pattern(regexp="^[a-zA-Z0-9 ]+$")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  
   /**
   * Financial Year for which the KPI is applicable
   * @return finYear
  **/
  @NotNull

 @Pattern(regexp="^[a-zA-Z0-9-]+$")
  public String getFinYear() {
    return finYear;
  }

  public void setFinYear(String finYear) {
    this.finYear = finYear;
  }

  public KPI documents(List<Document> documents) {
    this.documents = documents;
    return this;
  }

  public KPI addDocumentsItem(Document documentsItem) {
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

  public KPI createdBy(Long createdBy) {
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

  public KPI lastModifiedBy(Long lastModifiedBy) {
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

  public KPI createdDate(Long createdDate) {
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

  public KPI lastModifiedDate(Long lastModifiedDate) {
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
    KPI KPI = (KPI) o;
    return Objects.equals(this.id, KPI.id) &&
        Objects.equals(this.name, KPI.name) &&
        Objects.equals(this.code, KPI.code) &&
        Objects.equals(this.description, KPI.description) &&
        Objects.equals(this.finYear, KPI.finYear) &&
        Objects.equals(this.documents, KPI.documents) &&
        Objects.equals(this.createdBy, KPI.createdBy) &&
        Objects.equals(this.lastModifiedBy, KPI.lastModifiedBy) &&
        Objects.equals(this.createdDate, KPI.createdDate) &&
        Objects.equals(this.lastModifiedDate, KPI.lastModifiedDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, code, description,finYear, documents, createdBy, lastModifiedBy, createdDate, lastModifiedDate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class KPI {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    finYear: ").append(toIndentedString(finYear)).append("\n");
    sb.append("    documents: ").append(toIndentedString(documents)).append("\n");
    sb.append("    createdBy: ").append(toIndentedString(createdBy)).append("\n");
    sb.append("    lastModifiedBy: ").append(toIndentedString(lastModifiedBy)).append("\n");
    sb.append("    createdDate: ").append(toIndentedString(createdDate)).append("\n");
    sb.append("    lastModifiedDate: ").append(toIndentedString(lastModifiedDate)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  public KpiTarget getKpiTarget() {
	return kpiTarget;
}

public void setKpiTarget(KpiTarget kpiTarget) {
	this.kpiTarget = kpiTarget;
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

