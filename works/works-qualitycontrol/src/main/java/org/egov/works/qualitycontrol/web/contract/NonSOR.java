package org.egov.works.qualitycontrol.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * An Object that holds NON SOR details for a given Estimate template.
 */
@ApiModel(description = "An Object that holds NON SOR details for a given Estimate template.")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-12-16T15:20:43.552Z")

public class NonSOR   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("uom")
  private UOM uom = null;

  @JsonProperty("deleted")
  private Boolean deleted = false;

  public NonSOR id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Non SOR
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Non SOR")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public NonSOR tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the Non SOR
   * @return tenantId
  **/
  @ApiModelProperty(required = true, value = "Tenant id of the Non SOR")
  @NotNull

 @Size(min=2,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public NonSOR description(String description) {
    this.description = description;
    return this;
  }

   /**
   * Description of the Non SOR
   * @return description
  **/
  @ApiModelProperty(required = true, value = "Description of the Non SOR")
  @NotNull

 @Pattern(regexp="[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]+") @Size(min=1,max=1024)
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public NonSOR uom(UOM uom) {
    this.uom = uom;
    return this;
  }

   /**
   * UOM for the Non SOR. Unique reference from 'UOM'. Code is ref. here.
   * @return uom
  **/
  @ApiModelProperty(required = true, value = "UOM for the Non SOR. Unique reference from 'UOM'. Code is ref. here.")
  @NotNull

  @Valid

  public UOM getUom() {
    return uom;
  }

  public void setUom(UOM uom) {
    this.uom = uom;
  }

  public NonSOR deleted(Boolean deleted) {
    this.deleted = deleted;
    return this;
  }

   /**
   * Boolean value to identify whether the object is deleted or not from UI.
   * @return deleted
  **/
  @ApiModelProperty(value = "Boolean value to identify whether the object is deleted or not from UI.")


  public Boolean getDeleted() {
    return deleted;
  }

  public void setDeleted(Boolean deleted) {
    this.deleted = deleted;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NonSOR nonSOR = (NonSOR) o;
    return Objects.equals(this.id, nonSOR.id) &&
        Objects.equals(this.tenantId, nonSOR.tenantId) &&
        Objects.equals(this.description, nonSOR.description) &&
        Objects.equals(this.uom, nonSOR.uom) &&
        Objects.equals(this.deleted, nonSOR.deleted);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, description, uom, deleted);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NonSOR {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    uom: ").append(toIndentedString(uom)).append("\n");
    sb.append("    deleted: ").append(toIndentedString(deleted)).append("\n");
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

