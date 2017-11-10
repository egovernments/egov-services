package org.egov.works.masters.web.contract;

import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 */
@ApiModel(description = "")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-10T10:39:50.702Z")

public class Bank   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("code")
  private String code = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("active")
  private Boolean active = null;

  @JsonProperty("type")
  private String type = null;

  @JsonProperty("auditDetails")
  private Auditable auditDetails = null;

  public Bank id(String id) {
    this.id = id;
    return this;
  }

   /**
   * id is unique identifier . It is generated internally 
   * @return id
  **/
  @ApiModelProperty(value = "id is unique identifier . It is generated internally ")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Bank code(String code) {
    this.code = code;
    return this;
  }

   /**
   * code is the code of the bank 
   * @return code
  **/
  @ApiModelProperty(required = true, value = "code is the code of the bank ")
  @NotNull

 @Size(min=1,max=50)
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Bank name(String name) {
    this.name = name;
    return this;
  }

   /**
   * name is the Bank Name . 
   * @return name
  **/
  @ApiModelProperty(required = true, value = "name is the Bank Name . ")
  @NotNull

 @Size(min=2,max=100)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Bank description(String description) {
    this.description = description;
    return this;
  }

   /**
   * description more detailed description of the bank 
   * @return description
  **/
  @ApiModelProperty(value = "description more detailed description of the bank ")

 @Size(max=250)
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Bank active(Boolean active) {
    this.active = active;
    return this;
  }

   /**
   * active states whether the bank is active in the system or not . 
   * @return active
  **/
  @ApiModelProperty(required = true, value = "active states whether the bank is active in the system or not . ")
  @NotNull


  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public Bank type(String type) {
    this.type = type;
    return this;
  }

   /**
   * type of the Bank 
   * @return type
  **/
  @ApiModelProperty(required = true, value = "type of the Bank ")
  @NotNull

 @Size(max=50)
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Bank auditDetails(Auditable auditDetails) {
    this.auditDetails = auditDetails;
    return this;
  }

   /**
   * Get auditDetails
   * @return auditDetails
  **/
  @ApiModelProperty(value = "")

  @Valid

  public Auditable getAuditDetails() {
    return auditDetails;
  }

  public void setAuditDetails(Auditable auditDetails) {
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
    Bank bank = (Bank) o;
    return Objects.equals(this.id, bank.id) &&
        Objects.equals(this.code, bank.code) &&
        Objects.equals(this.name, bank.name) &&
        Objects.equals(this.description, bank.description) &&
        Objects.equals(this.active, bank.active) &&
        Objects.equals(this.type, bank.type) &&
        Objects.equals(this.auditDetails, bank.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, code, name, description, active, type, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Bank {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    active: ").append(toIndentedString(active)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
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

