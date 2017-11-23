package org.egov.works.measurementbook.web.contract;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.joda.time.LocalDate;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * UOM
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-23T09:58:12.227Z")

public class UOM   {
  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("code")
  private String code = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("category")
  private Object category = null;

  @JsonProperty("active")
  private Boolean active = true;

  @JsonProperty("coversionFactor")
  private Float coversionFactor = null;

  @JsonProperty("baseuom")
  private Boolean baseuom = false;

  @JsonProperty("createdBy")
  private String createdBy = null;

  @JsonProperty("createdDate")
  private LocalDate createdDate = null;

  @JsonProperty("lastModifiedBy")
  private String lastModifiedBy = null;

  @JsonProperty("lastModifiedDate")
  private LocalDate lastModifiedDate = null;

  public UOM id(Long id) {
    this.id = id;
    return this;
  }

   /**
   * Unique identifier of the UOM.
   * @return id
  **/
  @ApiModelProperty(value = "Unique identifier of the UOM.")


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public UOM code(String code) {
    this.code = code;
    return this;
  }

   /**
   * The code of the uom.
   * @return code
  **/
  @ApiModelProperty(required = true, value = "The code of the uom.")
  @NotNull

 @Size(min=1,max=30)
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public UOM description(String description) {
    this.description = description;
    return this;
  }

   /**
   * The description of the unit of measurement.
   * @return description
  **/
  @ApiModelProperty(value = "The description of the unit of measurement.")

 @Size(max=250)
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public UOM category(Object category) {
    this.category = category;
    return this;
  }

   /**
   * category to which this unit of measurement belongs.
   * @return category
  **/
  @ApiModelProperty(required = true, value = "category to which this unit of measurement belongs.")
  @NotNull


  public Object getCategory() {
    return category;
  }

  public void setCategory(Object category) {
    this.category = category;
  }

  public UOM active(Boolean active) {
    this.active = active;
    return this;
  }

   /**
   * TRUE for active languages and FALSE for inactive languages.
   * @return active
  **/
  @ApiModelProperty(required = true, value = "TRUE for active languages and FALSE for inactive languages.")
  @NotNull


  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public UOM coversionFactor(Float coversionFactor) {
    this.coversionFactor = coversionFactor;
    return this;
  }

   /**
   * What is the conversion factor of this unit of measurement with the base UOM. For Base UOM the conversion factor will be 1.
   * @return coversionFactor
  **/
  @ApiModelProperty(value = "What is the conversion factor of this unit of measurement with the base UOM. For Base UOM the conversion factor will be 1.")


  public Float getCoversionFactor() {
    return coversionFactor;
  }

  public void setCoversionFactor(Float coversionFactor) {
    this.coversionFactor = coversionFactor;
  }

  public UOM baseuom(Boolean baseuom) {
    this.baseuom = baseuom;
    return this;
  }

   /**
   * TRUE if this particular UOM is the base UOM and FALSE if it not the base UOM.
   * @return baseuom
  **/
  @ApiModelProperty(value = "TRUE if this particular UOM is the base UOM and FALSE if it not the base UOM.")


  public Boolean getBaseuom() {
    return baseuom;
  }

  public void setBaseuom(Boolean baseuom) {
    this.baseuom = baseuom;
  }

  public UOM createdBy(String createdBy) {
    this.createdBy = createdBy;
    return this;
  }

   /**
   * Id of the user who created the record.
   * @return createdBy
  **/
  @ApiModelProperty(value = "Id of the user who created the record.")


  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public UOM createdDate(LocalDate createdDate) {
    this.createdDate = createdDate;
    return this;
  }

   /**
   * Date on which the uom master data was added into the system.
   * @return createdDate
  **/
  @ApiModelProperty(value = "Date on which the uom master data was added into the system.")

  @Valid

  public LocalDate getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(LocalDate createdDate) {
    this.createdDate = createdDate;
  }

  public UOM lastModifiedBy(String lastModifiedBy) {
    this.lastModifiedBy = lastModifiedBy;
    return this;
  }

   /**
   * Id of the user who last modified the record.
   * @return lastModifiedBy
  **/
  @ApiModelProperty(value = "Id of the user who last modified the record.")


  public String getLastModifiedBy() {
    return lastModifiedBy;
  }

  public void setLastModifiedBy(String lastModifiedBy) {
    this.lastModifiedBy = lastModifiedBy;
  }

  public UOM lastModifiedDate(LocalDate lastModifiedDate) {
    this.lastModifiedDate = lastModifiedDate;
    return this;
  }

   /**
   * Date on which the uom master data was last modified.
   * @return lastModifiedDate
  **/
  @ApiModelProperty(value = "Date on which the uom master data was last modified.")

  @Valid

  public LocalDate getLastModifiedDate() {
    return lastModifiedDate;
  }

  public void setLastModifiedDate(LocalDate lastModifiedDate) {
    this.lastModifiedDate = lastModifiedDate;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UOM UOM = (UOM) o;
    return Objects.equals(this.id, UOM.id) &&
        Objects.equals(this.code, UOM.code) &&
        Objects.equals(this.description, UOM.description) &&
        Objects.equals(this.category, UOM.category) &&
        Objects.equals(this.active, UOM.active) &&
        Objects.equals(this.coversionFactor, UOM.coversionFactor) &&
        Objects.equals(this.baseuom, UOM.baseuom) &&
        Objects.equals(this.createdBy, UOM.createdBy) &&
        Objects.equals(this.createdDate, UOM.createdDate) &&
        Objects.equals(this.lastModifiedBy, UOM.lastModifiedBy) &&
        Objects.equals(this.lastModifiedDate, UOM.lastModifiedDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, code, description, category, active, coversionFactor, baseuom, createdBy, createdDate, lastModifiedBy, lastModifiedDate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UOM {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    active: ").append(toIndentedString(active)).append("\n");
    sb.append("    coversionFactor: ").append(toIndentedString(coversionFactor)).append("\n");
    sb.append("    baseuom: ").append(toIndentedString(baseuom)).append("\n");
    sb.append("    createdBy: ").append(toIndentedString(createdBy)).append("\n");
    sb.append("    createdDate: ").append(toIndentedString(createdDate)).append("\n");
    sb.append("    lastModifiedBy: ").append(toIndentedString(lastModifiedBy)).append("\n");
    sb.append("    lastModifiedDate: ").append(toIndentedString(lastModifiedDate)).append("\n");
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

