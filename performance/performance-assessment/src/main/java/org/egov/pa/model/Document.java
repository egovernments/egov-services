package org.egov.pa.model;

import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * documents required under respective KPI
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-02T05:16:16.756Z")

public class Document   {
  @JsonProperty("id")
  private String id = null; 
	
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
  
  

  public AuditDetails getAuditDetails() {
	return auditDetails;
}



public void setAuditDetails(AuditDetails auditDetails) {
	this.auditDetails = auditDetails;
}



public String getKpiCode() {
	return kpiCode;
}



public void setKpiCode(String kpiCode) {
	this.kpiCode = kpiCode;
}



public Document name(String name) {
    this.name = name;
    return this;
  }
  
  

   public String getId() {
	return id;
}



public void setId(String id) {
	this.id = id;
}



/**
   * name of the Document.
   * @return name
  **/

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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Document document = (Document) o;
    return Objects.equals(this.name, document.name) &&
        Objects.equals(this.code, document.code) &&
        Objects.equals(this.description, document.description) &&
        Objects.equals(this.active, document.active);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, code, description, active);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Document {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    active: ").append(toIndentedString(active)).append("\n");
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

