package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.AuditDetails;
import io.swagger.model.ScrapDetails;
import io.swagger.model.Store;
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

public class Scrap   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("store")
  private Store store = null;

  @JsonProperty("scrapNumber")
  private String scrapNumber = null;

  @JsonProperty("scrapDate")
  private Long scrapDate = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("scrapDetails")
  @Valid
  private List<ScrapDetails> scrapDetails = null;

  /**
   * scrap status of the Scrap 
   */
  public enum ScrapStatusEnum {
    CREATED("CREATED"),
    
    APPROVED("APPROVED"),
    
    REJECTED("REJECTED"),
    
    CANCELED("CANCELED");

    private String value;

    ScrapStatusEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static ScrapStatusEnum fromValue(String text) {
      for (ScrapStatusEnum b : ScrapStatusEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("scrapStatus")
  private ScrapStatusEnum scrapStatus = null;

  @JsonProperty("stateId")
  private Long stateId = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public Scrap id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Scrap 
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Scrap ")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Scrap tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the Scrap
   * @return tenantId
  **/
  @ApiModelProperty(value = "Tenant id of the Scrap")

 @Size(min=4,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public Scrap store(Store store) {
    this.store = store;
    return this;
  }

   /**
   * Get store
   * @return store
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public Store getStore() {
    return store;
  }

  public void setStore(Store store) {
    this.store = store;
  }

  public Scrap scrapNumber(String scrapNumber) {
    this.scrapNumber = scrapNumber;
    return this;
  }

   /**
   * scrapNumber  Auto generated number, read only 
   * @return scrapNumber
  **/
  @ApiModelProperty(required = true, readOnly = true, value = "scrapNumber  Auto generated number, read only ")
  @NotNull


  public String getScrapNumber() {
    return scrapNumber;
  }

  public void setScrapNumber(String scrapNumber) {
    this.scrapNumber = scrapNumber;
  }

  public Scrap scrapDate(Long scrapDate) {
    this.scrapDate = scrapDate;
    return this;
  }

   /**
   * scrap date of the Scrap 
   * @return scrapDate
  **/
  @ApiModelProperty(required = true, value = "scrap date of the Scrap ")
  @NotNull


  public Long getScrapDate() {
    return scrapDate;
  }

  public void setScrapDate(Long scrapDate) {
    this.scrapDate = scrapDate;
  }

  public Scrap description(String description) {
    this.description = description;
    return this;
  }

   /**
   * description of the Scrap 
   * @return description
  **/
  @ApiModelProperty(value = "description of the Scrap ")

 @Size(max=500)
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Scrap scrapDetails(List<ScrapDetails> scrapDetails) {
    this.scrapDetails = scrapDetails;
    return this;
  }

  public Scrap addScrapDetailsItem(ScrapDetails scrapDetailsItem) {
    if (this.scrapDetails == null) {
      this.scrapDetails = new ArrayList<ScrapDetails>();
    }
    this.scrapDetails.add(scrapDetailsItem);
    return this;
  }

   /**
   * scrap details of the Scrap 
   * @return scrapDetails
  **/
  @ApiModelProperty(value = "scrap details of the Scrap ")

  @Valid

  public List<ScrapDetails> getScrapDetails() {
    return scrapDetails;
  }

  public void setScrapDetails(List<ScrapDetails> scrapDetails) {
    this.scrapDetails = scrapDetails;
  }

  public Scrap scrapStatus(ScrapStatusEnum scrapStatus) {
    this.scrapStatus = scrapStatus;
    return this;
  }

   /**
   * scrap status of the Scrap 
   * @return scrapStatus
  **/
  @ApiModelProperty(value = "scrap status of the Scrap ")


  public ScrapStatusEnum getScrapStatus() {
    return scrapStatus;
  }

  public void setScrapStatus(ScrapStatusEnum scrapStatus) {
    this.scrapStatus = scrapStatus;
  }

  public Scrap stateId(Long stateId) {
    this.stateId = stateId;
    return this;
  }

   /**
   * state id of the Scrap 
   * @return stateId
  **/
  @ApiModelProperty(value = "state id of the Scrap ")


  public Long getStateId() {
    return stateId;
  }

  public void setStateId(Long stateId) {
    this.stateId = stateId;
  }

  public Scrap auditDetails(AuditDetails auditDetails) {
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
    Scrap scrap = (Scrap) o;
    return Objects.equals(this.id, scrap.id) &&
        Objects.equals(this.tenantId, scrap.tenantId) &&
        Objects.equals(this.store, scrap.store) &&
        Objects.equals(this.scrapNumber, scrap.scrapNumber) &&
        Objects.equals(this.scrapDate, scrap.scrapDate) &&
        Objects.equals(this.description, scrap.description) &&
        Objects.equals(this.scrapDetails, scrap.scrapDetails) &&
        Objects.equals(this.scrapStatus, scrap.scrapStatus) &&
        Objects.equals(this.stateId, scrap.stateId) &&
        Objects.equals(this.auditDetails, scrap.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, store, scrapNumber, scrapDate, description, scrapDetails, scrapStatus, stateId, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Scrap {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    store: ").append(toIndentedString(store)).append("\n");
    sb.append("    scrapNumber: ").append(toIndentedString(scrapNumber)).append("\n");
    sb.append("    scrapDate: ").append(toIndentedString(scrapDate)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    scrapDetails: ").append(toIndentedString(scrapDetails)).append("\n");
    sb.append("    scrapStatus: ").append(toIndentedString(scrapStatus)).append("\n");
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

