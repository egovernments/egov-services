package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.AuditDetails;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * 
 */
@ApiModel(description = "")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-28T13:21:55.964+05:30")

public class Supplier   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("code")
  private String code = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public Supplier id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Supplier 
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Supplier ")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Supplier tenantId(String tenantId) {
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

  public Supplier code(String code) {
    this.code = code;
    return this;
  }

   /**
   * code of the Supplier 
   * @return code
  **/
  @ApiModelProperty(value = "code of the Supplier ")


  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Supplier auditDetails(AuditDetails auditDetails) {
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
    Supplier supplier = (Supplier) o;
    return Objects.equals(this.id, supplier.id) &&
        Objects.equals(this.tenantId, supplier.tenantId) &&
        Objects.equals(this.code, supplier.code) &&
        Objects.equals(this.auditDetails, supplier.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, code, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Supplier {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
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

