package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.AuditDetails;
import io.swagger.model.Indent;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * 
 */
@ApiModel(description = "")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-28T13:21:55.964+05:30")

public class IndentNote   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("indent")
  private Indent indent = null;

  /**
   * Gets or Sets inventoryType
   */
  public enum InventoryTypeEnum {
    ASSET("Asset"),
    
    CONSUMABLE("Consumable");

    private String value;

    InventoryTypeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static InventoryTypeEnum fromValue(String text) {
      for (InventoryTypeEnum b : InventoryTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("inventoryType")
  private InventoryTypeEnum inventoryType = null;

  @JsonProperty("expectedDeliveryDate")
  private Long expectedDeliveryDate = null;

  @JsonProperty("stateId")
  private Long stateId = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public IndentNote id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Indent Note 
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Indent Note ")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public IndentNote tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the Indent Note
   * @return tenantId
  **/
  @ApiModelProperty(value = "Tenant id of the Indent Note")

 @Size(min=4,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public IndentNote indent(Indent indent) {
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

  public IndentNote inventoryType(InventoryTypeEnum inventoryType) {
    this.inventoryType = inventoryType;
    return this;
  }

   /**
   * Get inventoryType
   * @return inventoryType
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull


  public InventoryTypeEnum getInventoryType() {
    return inventoryType;
  }

  public void setInventoryType(InventoryTypeEnum inventoryType) {
    this.inventoryType = inventoryType;
  }

  public IndentNote expectedDeliveryDate(Long expectedDeliveryDate) {
    this.expectedDeliveryDate = expectedDeliveryDate;
    return this;
  }

   /**
   * expected delivery date for Indent Note 
   * @return expectedDeliveryDate
  **/
  @ApiModelProperty(required = true, value = "expected delivery date for Indent Note ")
  @NotNull


  public Long getExpectedDeliveryDate() {
    return expectedDeliveryDate;
  }

  public void setExpectedDeliveryDate(Long expectedDeliveryDate) {
    this.expectedDeliveryDate = expectedDeliveryDate;
  }

  public IndentNote stateId(Long stateId) {
    this.stateId = stateId;
    return this;
  }

   /**
   * state id of the Indent Note 
   * @return stateId
  **/
  @ApiModelProperty(value = "state id of the Indent Note ")


  public Long getStateId() {
    return stateId;
  }

  public void setStateId(Long stateId) {
    this.stateId = stateId;
  }

  public IndentNote auditDetails(AuditDetails auditDetails) {
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
    IndentNote indentNote = (IndentNote) o;
    return Objects.equals(this.id, indentNote.id) &&
        Objects.equals(this.tenantId, indentNote.tenantId) &&
        Objects.equals(this.indent, indentNote.indent) &&
        Objects.equals(this.inventoryType, indentNote.inventoryType) &&
        Objects.equals(this.expectedDeliveryDate, indentNote.expectedDeliveryDate) &&
        Objects.equals(this.stateId, indentNote.stateId) &&
        Objects.equals(this.auditDetails, indentNote.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, indent, inventoryType, expectedDeliveryDate, stateId, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IndentNote {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    indent: ").append(toIndentedString(indent)).append("\n");
    sb.append("    inventoryType: ").append(toIndentedString(inventoryType)).append("\n");
    sb.append("    expectedDeliveryDate: ").append(toIndentedString(expectedDeliveryDate)).append("\n");
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

