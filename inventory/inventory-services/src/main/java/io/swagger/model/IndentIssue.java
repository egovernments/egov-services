package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.AuditDetails;
import io.swagger.model.IndentIssueDetail;
import io.swagger.model.MaterialIssue;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * 
 */
@ApiModel(description = "")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-28T13:21:55.964+05:30")

public class IndentIssue   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("materialIssue")
  private MaterialIssue materialIssue = null;

  @JsonProperty("issuedToEmployee")
  private String issuedToEmployee = null;

  @JsonProperty("stateId")
  private String stateId = null;

  @JsonProperty("indentIssueDetails")
  @Valid
  private List<IndentIssueDetail> indentIssueDetails = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public IndentIssue id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Indent Issue 
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Indent Issue ")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public IndentIssue tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the Indent Issue
   * @return tenantId
  **/
  @ApiModelProperty(value = "Tenant id of the Indent Issue")

 @Size(min=4,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public IndentIssue materialIssue(MaterialIssue materialIssue) {
    this.materialIssue = materialIssue;
    return this;
  }

   /**
   * Get materialIssue
   * @return materialIssue
  **/
  @ApiModelProperty(value = "")

  @Valid

  public MaterialIssue getMaterialIssue() {
    return materialIssue;
  }

  public void setMaterialIssue(MaterialIssue materialIssue) {
    this.materialIssue = materialIssue;
  }

  public IndentIssue issuedToEmployee(String issuedToEmployee) {
    this.issuedToEmployee = issuedToEmployee;
    return this;
  }

   /**
   * issued to employee of the IndentIssue 
   * @return issuedToEmployee
  **/
  @ApiModelProperty(value = "issued to employee of the IndentIssue ")


  public String getIssuedToEmployee() {
    return issuedToEmployee;
  }

  public void setIssuedToEmployee(String issuedToEmployee) {
    this.issuedToEmployee = issuedToEmployee;
  }

  public IndentIssue stateId(String stateId) {
    this.stateId = stateId;
    return this;
  }

   /**
   * state id of the IndentIssue 
   * @return stateId
  **/
  @ApiModelProperty(value = "state id of the IndentIssue ")


  public String getStateId() {
    return stateId;
  }

  public void setStateId(String stateId) {
    this.stateId = stateId;
  }

  public IndentIssue indentIssueDetails(List<IndentIssueDetail> indentIssueDetails) {
    this.indentIssueDetails = indentIssueDetails;
    return this;
  }

  public IndentIssue addIndentIssueDetailsItem(IndentIssueDetail indentIssueDetailsItem) {
    if (this.indentIssueDetails == null) {
      this.indentIssueDetails = new ArrayList<IndentIssueDetail>();
    }
    this.indentIssueDetails.add(indentIssueDetailsItem);
    return this;
  }

   /**
   * indent issue details of the IndentIssue 
   * @return indentIssueDetails
  **/
  @ApiModelProperty(value = "indent issue details of the IndentIssue ")

  @Valid

  public List<IndentIssueDetail> getIndentIssueDetails() {
    return indentIssueDetails;
  }

  public void setIndentIssueDetails(List<IndentIssueDetail> indentIssueDetails) {
    this.indentIssueDetails = indentIssueDetails;
  }

  public IndentIssue auditDetails(AuditDetails auditDetails) {
    this.auditDetails = auditDetails;
    return this;
  }

   /**
   * Get auditDetails
   * @return auditDetails
  **/
  @ApiModelProperty(value = "")

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
    IndentIssue indentIssue = (IndentIssue) o;
    return Objects.equals(this.id, indentIssue.id) &&
        Objects.equals(this.tenantId, indentIssue.tenantId) &&
        Objects.equals(this.materialIssue, indentIssue.materialIssue) &&
        Objects.equals(this.issuedToEmployee, indentIssue.issuedToEmployee) &&
        Objects.equals(this.stateId, indentIssue.stateId) &&
        Objects.equals(this.indentIssueDetails, indentIssue.indentIssueDetails) &&
        Objects.equals(this.auditDetails, indentIssue.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, materialIssue, issuedToEmployee, stateId, indentIssueDetails, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IndentIssue {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    materialIssue: ").append(toIndentedString(materialIssue)).append("\n");
    sb.append("    issuedToEmployee: ").append(toIndentedString(issuedToEmployee)).append("\n");
    sb.append("    stateId: ").append(toIndentedString(stateId)).append("\n");
    sb.append("    indentIssueDetails: ").append(toIndentedString(indentIssueDetails)).append("\n");
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

