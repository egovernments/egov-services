package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.AuditDetails;
import io.swagger.model.Indent;
import io.swagger.model.Store;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * 
 */
@ApiModel(description = "")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-28T13:21:55.964+05:30")

public class TransferIndentNote   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("indent")
  private Indent indent = null;

  @JsonProperty("indentStore")
  private Store indentStore = null;

  @JsonProperty("stateId")
  private Long stateId = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public TransferIndentNote id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Transfer Indent Note 
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Transfer Indent Note ")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public TransferIndentNote tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the Transfer Indent Note
   * @return tenantId
  **/
  @ApiModelProperty(value = "Tenant id of the Transfer Indent Note")

 @Size(min=4,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public TransferIndentNote indent(Indent indent) {
    this.indent = indent;
    return this;
  }

   /**
   * Get indent
   * @return indent
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public Indent getIndent() {
    return indent;
  }

  public void setIndent(Indent indent) {
    this.indent = indent;
  }

  public TransferIndentNote indentStore(Store indentStore) {
    this.indentStore = indentStore;
    return this;
  }

   /**
   * Get indentStore
   * @return indentStore
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public Store getIndentStore() {
    return indentStore;
  }

  public void setIndentStore(Store indentStore) {
    this.indentStore = indentStore;
  }

  public TransferIndentNote stateId(Long stateId) {
    this.stateId = stateId;
    return this;
  }

   /**
   * state id of the TransferIndentNote 
   * @return stateId
  **/
  @ApiModelProperty(value = "state id of the TransferIndentNote ")


  public Long getStateId() {
    return stateId;
  }

  public void setStateId(Long stateId) {
    this.stateId = stateId;
  }

  public TransferIndentNote auditDetails(AuditDetails auditDetails) {
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
    TransferIndentNote transferIndentNote = (TransferIndentNote) o;
    return Objects.equals(this.id, transferIndentNote.id) &&
        Objects.equals(this.tenantId, transferIndentNote.tenantId) &&
        Objects.equals(this.indent, transferIndentNote.indent) &&
        Objects.equals(this.indentStore, transferIndentNote.indentStore) &&
        Objects.equals(this.stateId, transferIndentNote.stateId) &&
        Objects.equals(this.auditDetails, transferIndentNote.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, indent, indentStore, stateId, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TransferIndentNote {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    indent: ").append(toIndentedString(indent)).append("\n");
    sb.append("    indentStore: ").append(toIndentedString(indentStore)).append("\n");
    sb.append("    stateId: ").append(toIndentedString(stateId)).append("\n");
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

