package org.egov.works.measurementbook.web.contract;

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
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-12-20T10:00:39.005Z")

public class AccountDetailType   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("tableName")
  private String tableName = null;

  @JsonProperty("active")
  private Boolean active = null;

  @JsonProperty("fullyQualifiedName")
  private String fullyQualifiedName = null;

  @JsonProperty("auditDetails")
  private Auditable auditDetails = null;

  public AccountDetailType id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the AccountDetailType 
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the AccountDetailType ")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public AccountDetailType name(String name) {
    this.name = name;
    return this;
  }

   /**
   * name of the AccountDetailType 
   * @return name
  **/
  @ApiModelProperty(required = true, value = "name of the AccountDetailType ")
  @NotNull

 @Size(min=1,max=50)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public AccountDetailType description(String description) {
    this.description = description;
    return this;
  }

   /**
   * description of the AccountDetailType 
   * @return description
  **/
  @ApiModelProperty(required = true, value = "description of the AccountDetailType ")
  @NotNull

 @Size(min=1,max=50)
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public AccountDetailType tableName(String tableName) {
    this.tableName = tableName;
    return this;
  }

   /**
   * table name of the AccountDetailType 
   * @return tableName
  **/
  @ApiModelProperty(value = "table name of the AccountDetailType ")

 @Size(max=25)
  public String getTableName() {
    return tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  public AccountDetailType active(Boolean active) {
    this.active = active;
    return this;
  }

   /**
   * Whether AccountDetailType is Active or not. If the value is TRUE, then AccountDetailType is active,If the value is FALSE then AccountDetailType is inactive,Default value is TRUE 
   * @return active
  **/
  @ApiModelProperty(required = true, value = "Whether AccountDetailType is Active or not. If the value is TRUE, then AccountDetailType is active,If the value is FALSE then AccountDetailType is inactive,Default value is TRUE ")
  @NotNull


  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public AccountDetailType fullyQualifiedName(String fullyQualifiedName) {
    this.fullyQualifiedName = fullyQualifiedName;
    return this;
  }

   /**
   * fully qualified name of the AccountDetailType 
   * @return fullyQualifiedName
  **/
  @ApiModelProperty(value = "fully qualified name of the AccountDetailType ")

 @Size(min=1,max=250)
  public String getFullyQualifiedName() {
    return fullyQualifiedName;
  }

  public void setFullyQualifiedName(String fullyQualifiedName) {
    this.fullyQualifiedName = fullyQualifiedName;
  }

  public AccountDetailType auditDetails(Auditable auditDetails) {
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
    AccountDetailType accountDetailType = (AccountDetailType) o;
    return Objects.equals(this.id, accountDetailType.id) &&
        Objects.equals(this.name, accountDetailType.name) &&
        Objects.equals(this.description, accountDetailType.description) &&
        Objects.equals(this.tableName, accountDetailType.tableName) &&
        Objects.equals(this.active, accountDetailType.active) &&
        Objects.equals(this.fullyQualifiedName, accountDetailType.fullyQualifiedName) &&
        Objects.equals(this.auditDetails, accountDetailType.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description, tableName, active, fullyQualifiedName, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AccountDetailType {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    tableName: ").append(toIndentedString(tableName)).append("\n");
    sb.append("    active: ").append(toIndentedString(active)).append("\n");
    sb.append("    fullyQualifiedName: ").append(toIndentedString(fullyQualifiedName)).append("\n");
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

