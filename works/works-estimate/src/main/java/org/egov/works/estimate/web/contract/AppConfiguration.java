package org.egov.works.estimate.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.joda.time.LocalDate;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * this is model class for inserting app config data
 */
@ApiModel(description = "this is model class for inserting app config data")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-29T09:02:10.583Z")

public class AppConfiguration   {
  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("keyName")
  private String keyName = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("values")
  private List<String> values = new ArrayList<String>();

  @JsonProperty("effectiveFrom")
  private LocalDate effectiveFrom = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public AppConfiguration id(Long id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the App Configuration
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the App Configuration")


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public AppConfiguration tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * tenant id of the App Configuration
   * @return tenantId
  **/
  @ApiModelProperty(required = true, value = "tenant id of the App Configuration")
  @NotNull

 @Size(min=2,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public AppConfiguration keyName(String keyName) {
    this.keyName = keyName;
    return this;
  }

   /**
   * name of the key
   * @return keyName
  **/
  @ApiModelProperty(required = true, value = "name of the key")
  @NotNull

 @Size(min=1,max=256)
  public String getKeyName() {
    return keyName;
  }

  public void setKeyName(String keyName) {
    this.keyName = keyName;
  }

  public AppConfiguration description(String description) {
    this.description = description;
    return this;
  }

   /**
   * description about the key
   * @return description
  **/
  @ApiModelProperty(value = "description about the key")

 @Size(min=1,max=256)
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public AppConfiguration values(List<String> values) {
    this.values = values;
    return this;
  }

  public AppConfiguration addValuesItem(String valuesItem) {
    this.values.add(valuesItem);
    return this;
  }

   /**
   * array of values corresponding to key
   * @return values
  **/
  @ApiModelProperty(required = true, value = "array of values corresponding to key")
  @NotNull


  public List<String> getValues() {
    return values;
  }

  public void setValues(List<String> values) {
    this.values = values;
  }

  public AppConfiguration effectiveFrom(LocalDate effectiveFrom) {
    this.effectiveFrom = effectiveFrom;
    return this;
  }

   /**
   * Get effectiveFrom
   * @return effectiveFrom
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public LocalDate getEffectiveFrom() {
    return effectiveFrom;
  }

  public void setEffectiveFrom(LocalDate effectiveFrom) {
    this.effectiveFrom = effectiveFrom;
  }

  public AppConfiguration auditDetails(AuditDetails auditDetails) {
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
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AppConfiguration appConfiguration = (AppConfiguration) o;
    return Objects.equals(this.id, appConfiguration.id) &&
        Objects.equals(this.tenantId, appConfiguration.tenantId) &&
        Objects.equals(this.keyName, appConfiguration.keyName) &&
        Objects.equals(this.description, appConfiguration.description) &&
        Objects.equals(this.values, appConfiguration.values) &&
        Objects.equals(this.effectiveFrom, appConfiguration.effectiveFrom) &&
        Objects.equals(this.auditDetails, appConfiguration.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, keyName, description, values, effectiveFrom, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AppConfiguration {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    keyName: ").append(toIndentedString(keyName)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    values: ").append(toIndentedString(values)).append("\n");
    sb.append("    effectiveFrom: ").append(toIndentedString(effectiveFrom)).append("\n");
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

