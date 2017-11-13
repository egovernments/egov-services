package org.egov.lams.common.web.contract;

import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * This object holds type of documents to be uploaded during the transaction for each application type.
 */
@ApiModel(description = "This object holds type of documents to be uploaded during the transaction for each application type.")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-09T07:10:49.937Z")

public class DocumentType   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("name")
  private String name = null;

  /**
   * Application type.
   */
  public enum ApplicationEnum {
    AGREEMENT_CREATE("AGREEMENT_CREATE"),
    
    AGREEMENT_RENEWAL("AGREEMENT_RENEWAL"),
    
    AGREEMENT_EVICTION("AGREEMENT_EVICTION"),
    
    AGREEMENT_CANCEL("AGREEMENT_CANCEL"),
    
    LAND_CREATE("LAND_CREATE");

    private String value;

    ApplicationEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static ApplicationEnum fromValue(String text) {
      for (ApplicationEnum b : ApplicationEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("application")
  private ApplicationEnum application = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public DocumentType id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the DocumentType.
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the DocumentType.")

 @Size(min=1,max=256)
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public DocumentType tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * tenant id of the Property
   * @return tenantId
  **/
  @ApiModelProperty(value = "tenant id of the Property")

 @Size(min=2,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public DocumentType name(String name) {
    this.name = name;
    return this;
  }

   /**
   * name of the document
   * @return name
  **/
  @ApiModelProperty(value = "name of the document")


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public DocumentType application(ApplicationEnum application) {
    this.application = application;
    return this;
  }

   /**
   * Application type.
   * @return application
  **/
  @ApiModelProperty(value = "Application type.")


  public ApplicationEnum getApplication() {
    return application;
  }

  public void setApplication(ApplicationEnum application) {
    this.application = application;
  }

  public DocumentType auditDetails(AuditDetails auditDetails) {
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
    DocumentType documentType = (DocumentType) o;
    return Objects.equals(this.id, documentType.id) &&
        Objects.equals(this.tenantId, documentType.tenantId) &&
        Objects.equals(this.name, documentType.name) &&
        Objects.equals(this.application, documentType.application) &&
        Objects.equals(this.auditDetails, documentType.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, name, application, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DocumentType {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    application: ").append(toIndentedString(application)).append("\n");
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

