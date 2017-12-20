package org.egov.works.qualitycontrol.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * 
 */
@ApiModel(description = "")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-12-16T15:20:43.552Z")

public class Function   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("code")
  private String code = null;

  @JsonProperty("level")
  private Integer level = null;

  @JsonProperty("active")
  private Boolean active = null;

  @JsonProperty("parentId")
  private Long parentId = null;

  @JsonProperty("auditDetails")
  private Auditable auditDetails = null;

  public Function id(String id) {
    this.id = id;
    return this;
  }

   /**
   * id is the unique identifier . 
   * @return id
  **/
  @ApiModelProperty(value = "id is the unique identifier . ")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Function name(String name) {
    this.name = name;
    return this;
  }

   /**
   * name is the name of the function . 
   * @return name
  **/
  @ApiModelProperty(required = true, value = "name is the name of the function . ")
  @NotNull

 @Size(min=2,max=128)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Function code(String code) {
    this.code = code;
    return this;
  }

   /**
   * code is a unique number given to each function . ULBs may refer this for the short name. 
   * @return code
  **/
  @ApiModelProperty(required = true, value = "code is a unique number given to each function . ULBs may refer this for the short name. ")
  @NotNull

 @Size(min=2,max=16)
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Function level(Integer level) {
    this.level = level;
    return this;
  }

   /**
   * level identifies what is the level of the function in the tree structure. Top most parent will have level 0 and its child will have level as 1 
   * @return level
  **/
  @ApiModelProperty(required = true, value = "level identifies what is the level of the function in the tree structure. Top most parent will have level 0 and its child will have level as 1 ")
  @NotNull


  public Integer getLevel() {
    return level;
  }

  public void setLevel(Integer level) {
    this.level = level;
  }

  public Function active(Boolean active) {
    this.active = active;
    return this;
  }

   /**
   * active is a boolean value which says whether function is in use or not . If Function is active, then accounting of transactions under the Function is enabled. If Function becomes inactive, and no transactions can be accounted under the Function. Only leaf function can be used in transaction ie function which is not parent to any other function 
   * @return active
  **/
  @ApiModelProperty(required = true, value = "active is a boolean value which says whether function is in use or not . If Function is active, then accounting of transactions under the Function is enabled. If Function becomes inactive, and no transactions can be accounted under the Function. Only leaf function can be used in transaction ie function which is not parent to any other function ")
  @NotNull


  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public Function parentId(Long parentId) {
    this.parentId = parentId;
    return this;
  }

   /**
   * parent id of the Function 
   * @return parentId
  **/
  @ApiModelProperty(value = "parent id of the Function ")


  public Long getParentId() {
    return parentId;
  }

  public void setParentId(Long parentId) {
    this.parentId = parentId;
  }

  public Function auditDetails(Auditable auditDetails) {
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
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Function function = (Function) o;
    return Objects.equals(this.id, function.id) &&
        Objects.equals(this.name, function.name) &&
        Objects.equals(this.code, function.code) &&
        Objects.equals(this.level, function.level) &&
        Objects.equals(this.active, function.active) &&
        Objects.equals(this.parentId, function.parentId) &&
        Objects.equals(this.auditDetails, function.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, code, level, active, parentId, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Function {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    level: ").append(toIndentedString(level)).append("\n");
    sb.append("    active: ").append(toIndentedString(active)).append("\n");
    sb.append("    parentId: ").append(toIndentedString(parentId)).append("\n");
    sb.append("    auditDetails: ").append(toIndentedString(auditDetails)).append("\n");
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

