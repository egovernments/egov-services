package org.egov.pa.model;

import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * documents required under respective KPI
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-30T06:35:11.343Z")

public class Document   {
  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("code")
  private String code = null;
  
  @JsonProperty("kpiCode")
  private String kpiCode = null; 

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("active")
  private Boolean active = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  
  
  public String getKpiCode() {
	return kpiCode;
}

public void setKpiCode(String kpiCode) {
	this.kpiCode = kpiCode;
}

public Document id(Long id) {
    this.id = id;
    return this;
  }

   /**
   * Unique identifier of the Document.
   * @return id
  **/


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Document name(String name) {
    this.name = name;
    return this;
  }

   /**
   * name of the Document.
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

  public Document code(String code) {
    this.code = code;
    return this;
  }

   /**
   * Unique Code of the Document.
   * @return code
  **/

 @Pattern(regexp="^[a-zA-Z0-9]+$")
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Document description(String description) {
    this.description = description;
    return this;
  }

   /**
   * description of the Document.
   * @return description
  **/
 @Pattern(regexp="^[a-zA-Z0-9 ]+$") @Size(max=250)
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Document active(Boolean active) {
    this.active = active;
    return this;
  }

   /**
   * TRUE for active Document Names and FALSE for inactive Document Names.
   * @return active
  **/
  @NotNull


  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public Document auditDetails(AuditDetails auditDetails) {
    this.auditDetails = auditDetails;
    return this;
  }

   /**
   * Get auditDetails
   * @return auditDetails
  **/

  @Valid

  public AuditDetails getAuditDetails() {
    return auditDetails;
  }

  public void setAuditDetails(AuditDetails auditDetails) {
    this.auditDetails = auditDetails;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Document document = (Document) o;
    return Objects.equals(this.id, document.id) &&
        Objects.equals(this.name, document.name) &&
        Objects.equals(this.code, document.code) &&
        Objects.equals(this.description, document.description) &&
        Objects.equals(this.active, document.active) &&
        Objects.equals(this.auditDetails, document.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, code, description, active, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Document {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    active: ").append(toIndentedString(active)).append("\n");
    sb.append("    auditDetails: ").append(toIndentedString(auditDetails)).append("\n");
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

