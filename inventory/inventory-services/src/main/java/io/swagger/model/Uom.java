package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.AuditDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Unit of Measurement 
 */
@ApiModel(description = "Unit of Measurement ")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-28T13:21:55.964+05:30")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Uom   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("code")
  private String code = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public Uom id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the uom 
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the uom ")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Uom tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the uom
   * @return tenantId
  **/
  @ApiModelProperty(value = "Tenant id of the uom")

 @Size(min=4,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public Uom name(String name) {
    this.name = name;
    return this;
  }

   /**
   * name of the uom 
   * @return name
  **/
  @ApiModelProperty(value = "name of the uom ")


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Uom code(String code) {
    this.code = code;
    return this;
  }

   /**
   * code of the uom 
   * @return code
  **/
  @ApiModelProperty(value = "code of the uom ")


  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Uom auditDetails(AuditDetails auditDetails) {
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
    Uom uom = (Uom) o;
    return Objects.equals(this.id, uom.id) &&
        Objects.equals(this.tenantId, uom.tenantId) &&
        Objects.equals(this.name, uom.name) &&
        Objects.equals(this.code, uom.code) &&
        Objects.equals(this.auditDetails, uom.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, name, code, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Uom {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
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

