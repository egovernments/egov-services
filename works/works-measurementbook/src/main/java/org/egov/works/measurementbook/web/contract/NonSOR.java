package org.egov.works.measurementbook.web.contract;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * An Object that holds NON SOR details for a given Estimate template.
 */
@ApiModel(description = "An Object that holds NON SOR details for a given Estimate template.")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-23T09:58:12.227Z")

public class NonSOR   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("uom")
  private String uom = null;

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

  public NonSOR uom(String uom) {
    this.uom = uom;
    return this;
  }

   /**
   * UOM for the Non SOR. Unique reference from 'UOM'. Code is ref. here.
   * @return uom
  **/
  @ApiModelProperty(required = true, value = "UOM for the Non SOR. Unique reference from 'UOM'. Code is ref. here.")
  @NotNull


  public String getUom() {
    return uom;
  }

  public void setUom(String uom) {
    this.uom = uom;
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
        Objects.equals(this.uom, nonSOR.uom);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, description, uom);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NonSOR {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    uom: ").append(toIndentedString(uom)).append("\n");
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

